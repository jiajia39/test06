package com.framework.emt.system.domain.task.response.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cola.extension.Extension;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.entity.FtUser;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.messages.service.MessagePushService;
import com.framework.emt.system.domain.task.response.request.TaskResponseTransferRequest;
import com.framework.emt.system.domain.task.response.request.TaskResponseUpdateRequest;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.handing.service.ExceptionTaskHandingService;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.response.constant.code.TaskResponseErrorCode;
import com.framework.emt.system.infrastructure.exception.task.response.service.ExceptionTaskResponseService;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


/**
 * 异常响应节点服务
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@Extension(bizId = "response")
@RequiredArgsConstructor
public class TaskResponseServiceImpl implements TaskResponseServiceExtPt {

    private final ExceptionTaskResponseService exceptionTaskResponseService;
    private final ExceptionTaskService exceptionTaskService;
    private final ExceptionTaskSettingService settingService;
    private final ExceptionTaskRecordService exceptionTaskRecordService;
    private final ExceptionTaskSubmitService taskSubmitService;
    private final ExceptionTaskHandingService handingService;
    private final UserService userService;
    private final MessagePushService messagePushService;
    private final IMessageService messageService;
    private final ExceptionTaskScheduleService exceptionTaskScheduleService;
    private final ExceptionItemService exceptionItemService;
    private final ExceptionTaskSettingService exceptionTaskSettingService;

    private final IFormFieldUseService formFieldUseService;

    @Override
    @DSTransactional
    public synchronized Long reject(Long id, String rejectReason, FtUser user) {
        ExceptionTaskResponse taskResponse = exceptionTaskResponseService.getByIdAndUserId(id, user.getUserId());
        // 异常任务响应驳回时，任务状态必须是待响应或这已转派
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusResponse(taskResponse.getExceptionTaskId());
        //  获取提报表信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 异常任务表 更新异常状态 提报已驳回
        exceptionTask.responseReject();
        exceptionTaskService.update(exceptionTask);

        //  更新提报表  填写驳回信息
        TaskRejectNode taskRejectNode = TaskRejectNode.RESPONSE;
        Long taskResponseId = taskResponse.getId();
        LocalDateTime currentTime = LocalDateTime.now();
        // 复制异常提报，提报版本+1
        TaskRejectRequest taskRejectRequest = new TaskRejectRequest(taskRejectNode, taskResponseId, currentTime, rejectReason, user.getUserId());
        taskSubmitService.copy(taskSubmit, taskRejectRequest, exceptionTask.getSubmitVersion());
        // [操作人]驳回异常提报，驳回原因:[拒绝原因]
        exceptionTaskRecordService.responseNodeReject(AuthUtil.getUser(), exceptionTask, taskResponse.getId(), taskSubmit, currentTime, rejectReason);

        // 发送驳回消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.responseRejectSend(user, exceptionTask.getId(), itemTitle, taskSubmit.getSubmitUserId(), id);
        // 定时消息取消发送
        exceptionTaskScheduleService.timeOutMessageSendCancel(taskSubmit, currentTime);

        return taskResponse.getId();
    }

    @Override
    @DSTransactional
    public synchronized Long toOther(Long id, FtUser currentUser, String otherRemark, Long userId) {
        // 验证响应转派人不能设置成自己,并验证转派人是否存在
        User transferUser = userService.userIdConsistent(userId, currentUser.getUserId(),
                TaskResponseErrorCode.THE_RESPONSE_SENDER_CANNOT_BE_SET_AS_THEMSELVES,
                TaskResponseErrorCode.NO_RESPONSE_TRANSFEROR_FOUND);
        ExceptionTaskResponse taskResponse = exceptionTaskResponseService.getByIdAndUserId(id, currentUser.getUserId());
        return updateTransferInfo(taskResponse, transferUser, otherRemark, currentUser);
    }

    @Override
    @DSTransactional
    public synchronized Long accept(Long id, Long userId) {
        ExceptionTaskResponse taskResponse = exceptionTaskResponseService.getByIdAndUserId(id, userId);
        //异常任务响应转派时，任务状态必须是待响应或者已转派
        ExceptionTask task = exceptionTaskService.validateStatusResponse(taskResponse.getExceptionTaskId());

        TaskResponseUpdateRequest taskResponseUpdateRequest = new TaskResponseUpdateRequest();
        LocalDateTime acceptTime = LocalDateTime.now();
        taskResponseUpdateRequest.responseAccept(userId, acceptTime);
        Long responseId = exceptionTaskResponseService.updateResponse(taskResponse, taskResponseUpdateRequest);

        // 异常任务表 更新异常状态 改为响应中
        task.responseAccept();
        exceptionTaskService.updateById(task);
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(task.getId(), task.getSubmitVersion());
        // 履历内容
        exceptionTaskRecordService.responseAccept(AuthUtil.getUser(), task, id, taskResponse, acceptTime);
        // 取消其他人响应超时任务
        List<Long> userIds = exceptionTaskResponseService.getRespondent(task.getId(), id, task.getResponseVersion());
        if (ObjectUtil.isNotEmpty(userIds)) {
            exceptionTaskScheduleService.responseTimeOutMessageCancelReceive(taskSubmit, acceptTime, userIds);
            exceptionTaskScheduleService.handingTimeOutMessageSendCancelByReceive(taskSubmit, acceptTime, userIds);
            //删除多余的响应数据
            exceptionTaskResponseService.deleteResponse(task.getId(), id, task.getResponseVersion());
        }
        return responseId;
    }

    @Override
    @DSTransactional
    public synchronized Long submit(Long id, Long submitHandingUserId, @Validated List<ExtendFieldsRequest> submitExtendDatas) {
        FtUser user = AuthUtil.getUser();
        // 获取异常任务
        ExceptionTaskResponse exceptionTaskResponse = exceptionTaskResponseService.getByIdAndUserId(id, user.getUserId());
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusResponseHanding(exceptionTaskResponse.getExceptionTaskId());
        // 获取异常提报
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 获取异常配置
        ExceptionTaskSetting setting = settingService.info(exceptionTask.getExceptionTaskSettingId());
        // 校验附加字段
        List<FormFieldResponse> extendDataList = settingService.validateExtendFields(exceptionTaskResponse.getSubmitExtendDatas(), submitExtendDatas);
        // 校验响应模式响应人
        List<Long> submitHandingUserIds = settingService.validateHandingUser(setting, user.getUserId(), submitHandingUserId);
        if (HandingModeEnum.NOT_SPECIFIED.equals(setting.getHandingMode())
                || HandingModeEnum.RESPONSE_AND_HANDING.equals(setting.getHandingMode())) {
            setting.setHandingUserIds(submitHandingUserIds);
            exceptionTaskSettingService.update(setting);
        }
        // 异常响应更新提交
        LocalDateTime currentTime = LocalDateTime.now();
        exceptionTaskResponseService.update(exceptionTaskResponse.submit(extendDataList, currentTime));
        // 异常任务表 更新异常状态 改为待处理
        LocalDateTime createTime = LocalDateTime.ofInstant(exceptionTaskResponse.getCreateTime().toInstant(), ZoneId.systemDefault());
        exceptionTask.responseSubmit(createTime, exceptionTaskResponse.getSubmitTime());
        exceptionTaskService.update(exceptionTask);
        //创建处理信息
        handingService.create(exceptionTask.getId(), exceptionTask.getHandingVersion(), submitHandingUserIds, setting.getHandingExtendFields(), setting.getPendingExtendFields());
        //履历内容  [操作人]提交异常响应给[处理人]
        List<User> users = userService.findUsers(submitHandingUserIds);
        exceptionTaskRecordService.responseHanding(user, users, exceptionTask, id, exceptionTaskResponse);

        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.responseSubmitSend(user, exceptionTask.getId(), itemTitle, submitHandingUserId, id);
        // 定时消息取消发送
        exceptionTaskScheduleService.responseTimeOutMessageSendCancel(taskSubmit, currentTime);
        if (!ObjectUtil.equal(setting.getHandingMode(), HandingModeEnum.MULTIPLE_PERSONNEL)) {
            // 修改处理超时消息接收人
            Integer businessType = ExceptionType.HANDING.getCode();
            messageService.updateReceiveUser(businessType, taskSubmit.getId(), submitHandingUserIds.get(0), null);
            // 修改异常任务定时计划接收人
            TimeOutType timeOutType = TimeOutType.HANDING;
            exceptionTaskScheduleService.updateAcceptUser(timeOutType, taskSubmit.getId(), submitHandingUserIds.get(0), null);
        }
        //添加异常单字段使用信息
        formFieldUseService.createFormFieldUser(extendDataList, exceptionTaskResponse.getId(), BusinessTypeEnum.EXCEPTION_RESPONSE);
        return exceptionTaskResponse.getId();
    }

    @Override
    @DSTransactional
    public Long adminResponseTransfer(Long taskId, TaskResponseTransferRequest request) {
        //判断是否有转派权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(TaskResponseErrorCode.CURRENT_USER_DOES_NOT_HAVE_ABNORMAL_MANAGEMENT_RESPONSE_TRANSFER_PERMISSION);
        }
        //获取响应信息
        ExceptionTask task = exceptionTaskService.findByIdThrowErr(taskId);
        ExceptionTaskResponse taskResponse = exceptionTaskResponseService.infoByTask(taskId, task.getResponseVersion());
        // 验证响应转派人是否是当前响应信息的响应人,并验证转派人是否存在
        User transferUser = userService.userIdConsistent(request.getUserId(), taskResponse.getUserId(), TaskResponseErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_ABNORMAL_MANAGEMENT_RESPONSE_TRANSFER_PERMISSION, TaskResponseErrorCode.NO_RESPONSE_TRANSFEROR_FOUND);
        return updateTransferInfo(taskResponse, transferUser, request.getOtherRemark(), AuthUtil.getUser());
    }

    /**
     * 转派
     *
     * @param taskResponse 响应信息
     * @param transferUser 转派人
     * @param otherRemark  转派备注
     * @param currentUser  当前用户
     * @param taskResponse 响应信息
     * @return
     */
    public Long updateTransferInfo(ExceptionTaskResponse taskResponse, User transferUser, String otherRemark, FtUser currentUser) {
        Long oldUserId = taskResponse.getUserId();
        //异常任务响应转派时，任务状态必须是待响应或者已转派，以及验证转派次数是否已超过最大值
        ExceptionTask exceptionTask = exceptionTaskService.validResponseToOther(taskResponse.getExceptionTaskId());
        // 获取异常提报
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        //更新响应表转派信息
        TaskResponseUpdateRequest taskResponseUpdateRequest = new TaskResponseUpdateRequest();
        taskResponseUpdateRequest.responseTransfer(transferUser.getId(), otherRemark, LocalDateTime.now());
        exceptionTaskResponseService.updateResponse(taskResponse, taskResponseUpdateRequest);
        //任务表更新 响应转派次数
        exceptionTask.responseTransfer();
        exceptionTaskService.updateById(exceptionTask);

        // 取消其他人响应超时任务
        List<Long> userIds = exceptionTaskResponseService.getRespondent(exceptionTask.getId(), taskResponse.getId(), exceptionTask.getResponseVersion());
        if (ObjectUtil.isNotEmpty(userIds)) {
            exceptionTaskScheduleService.responseTimeOutMessageCancelReceive(taskSubmit, taskResponseUpdateRequest.getOtherTime(), userIds);
            exceptionTaskScheduleService.handingTimeOutMessageSendCancelByReceive(taskSubmit, taskResponseUpdateRequest.getOtherTime(), userIds);
            //删除多余的响应数据
            exceptionTaskResponseService.deleteResponse(exceptionTask.getId(), taskResponse.getId(), exceptionTask.getResponseVersion());
        }

        //履历内容  [操作人]将异常转派给[新响应人]
        exceptionTaskRecordService.responseTransfer(currentUser, transferUser, exceptionTask, taskResponse.getId(), taskResponseUpdateRequest.getOtherTime());
        // 发送转派消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.responseToOtherSend(currentUser, exceptionTask.getId(), itemTitle, transferUser.getId(), taskResponse.getId());

        // 修改消息接收人
        Integer businessType = ExceptionType.RESPONSE.getCode();
        Integer handingBusinessType = ExceptionType.HANDING.getCode();
        messageService.updateReceiveUser(businessType, taskSubmit.getId(), transferUser, oldUserId);
        messageService.updateReceiveUser(handingBusinessType, taskSubmit.getId(), transferUser, oldUserId);
        // 修改异常任务定时计划接收人
        List<TimeOutType> timeOutTypes = TimeOutType.getResponses();
        exceptionTaskScheduleService.updateAcceptUser(timeOutTypes, taskSubmit.getId(), transferUser, oldUserId);
        exceptionTaskScheduleService.updateAcceptUser(TimeOutType.getHandings(), taskSubmit.getId(), transferUser, oldUserId);
        return taskResponse.getId();
    }

}

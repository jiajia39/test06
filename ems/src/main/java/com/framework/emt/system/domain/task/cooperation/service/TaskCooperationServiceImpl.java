package com.framework.emt.system.domain.task.cooperation.service;

import com.alibaba.cola.extension.Extension;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.framework.admin.system.entity.User;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.messages.service.MessagePushService;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationSubmitRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationTransferRequest;
import com.framework.emt.system.domain.task.cooperation.response.CooperationStatusNumResponse;
import com.framework.emt.system.domain.user.code.UserErrorCode;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.handing.service.ExceptionTaskHandingService;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常协同节点服务
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Extension(bizId = "cooperation")
@RequiredArgsConstructor
public class TaskCooperationServiceImpl implements TaskCooperationServiceExtPt {

    private final ExceptionTaskCooperationService exceptionTaskCooperationService;

    private final ExceptionTaskRecordService exceptionTaskRecordService;

    private final UserService userService;

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskSettingService settingService;

    private final ExceptionTaskHandingService handingService;

    private final MessagePushService messagePushService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    private final IMessageService messageService;

    private final ExceptionTaskSubmitService exceptionTaskSubmitService;

    private final ExceptionItemService exceptionItemService;


    private final IFormFieldUseService formFieldUseService;

    @Override
    @DSTransactional
    public void transfer(Long id, TaskCooperationTransferRequest request, FtUser user) {
        // 获取转派人
        User otherUser = userService.findUserByIdThrowErr(request.getUserId(), UserErrorCode.TRANSFER_USER_INFO_NOT_FIND);
        // 获取并校验协同状态是否可以转派
        ExceptionTaskCooperation taskCooperation = exceptionTaskCooperationService.validateStatusTransfer(id, request.getUserId(), user.getUserId());
        // 获取异常任务
        ExceptionTask task = exceptionTaskService.findByIdThrowErr(taskCooperation.getExceptionTaskId());
        // 获取异常处理
        ExceptionTaskHanding taskHanding = handingService.validateTransferNumber(taskCooperation.getExceptionTaskHandingId());

        // 更新处理信息 协同转派次数
        handingService.update(taskHanding.updateCooVer());
        // 更新异常协同
        exceptionTaskCooperationService.update(taskCooperation.transferInit(request, LocalDateTime.now()));
        // 添加履历
        exceptionTaskRecordService.cooperationNodeTransfer(user, task, taskCooperation, otherUser);

        // 发送消息
        String itemTitle = exceptionItemService.findItemName(task.getId(), task.getSubmitVersion());
        messagePushService.cooperationTransferSend(user, task.getId(), itemTitle, request.getUserId(), id);

        // 修改超时消息发送人
        Integer businessType = ExceptionType.COOPERATION.getCode();
        messageService.updateReceiveUser(businessType, id, otherUser, null);
        // 修改异常任务定时计划接收人
        List<TimeOutType> timeOutTypes = TimeOutType.getCooperations();
        exceptionTaskScheduleService.updateAcceptUser(timeOutTypes, id, otherUser, null);
    }

    @Override
    @DSTransactional
    public void accept(Long id, FtUser user) {
        // 获取并校验协同状态是否可以接收
        ExceptionTaskCooperation taskCooperation = exceptionTaskCooperationService.validateStatusAccept(id, user.getUserId());
        // 获取异常任务
        ExceptionTask exceptionTask = exceptionTaskService.findByIdThrowErr(taskCooperation.getExceptionTaskId());

        // 更新异常协同
        LocalDateTime currentTime = LocalDateTime.now();
        exceptionTaskCooperationService.update(taskCooperation.receiveInit(user.getUserId(), currentTime));
        // 添加履历
        exceptionTaskRecordService.cooperationNodeAccept(user, exceptionTask, taskCooperation);
    }

    @Override
    @DSTransactional
    public void submit(Long id, TaskCooperationSubmitRequest request, FtUser user) {
        // 获取并校验协同是否可以提交
        ExceptionTaskCooperation taskCooperation = exceptionTaskCooperationService.validateSubmit(id, user.getUserId());
        // 获取异常任务
        ExceptionTask exceptionTask = exceptionTaskService.findByIdThrowErr(taskCooperation.getExceptionTaskId());
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = settingService.validateExtendFields(taskCooperation.getSubmitExtendDatas(), request.getSubmitExtendDatas());

        // 更新异常协同
        LocalDateTime currentTime = LocalDateTime.now();
        exceptionTaskCooperationService.update(taskCooperation.submitInit(request, currentTime, extendDataList));
        //添加异常单字段使用信息
        formFieldUseService.createFormFieldUser(extendDataList, taskCooperation.getId(), BusinessTypeEnum.EXCEPTION_COLLABORATION);
        // 添加履历
        exceptionTaskRecordService.cooperationNodeSubmit(user, exceptionTask, taskCooperation);
        // 取消发送协同、协同上报超时定时消息
        exceptionTaskScheduleService.cooperationTimeOutMessageSendCancel(taskCooperation, currentTime);
    }

    @Override
    public CooperationStatusNumResponse statistics(Long currentUserId) {
        return exceptionTaskCooperationService.statistics(currentUserId);
    }

    @Override
    public void cancel(Long id, FtUser user) {
        // 获取并校验协同状态是否可以撤销
        ExceptionTaskCooperation taskCooperation = exceptionTaskCooperationService.validateStatusWaitCooperation(id);
        // 判断协同任务是否是当前用户的处理信息下的
        handingService.info(taskCooperation.getExceptionTaskHandingId(), user.getUserId());
        // 获取异常任务
        ExceptionTask exceptionTask = exceptionTaskService.findByIdThrowErr(taskCooperation.getExceptionTaskId());
        // 更新协同信息
        LocalDateTime currentTime = LocalDateTime.now();
        exceptionTaskCooperationService.update(taskCooperation.cancel(currentTime));
        // 添加履历
        exceptionTaskRecordService.cooperationNodeCancel(user, exceptionTask, taskCooperation);
        // 取消发送协同超时和协同上报超时定时消息
        exceptionTaskScheduleService.cooperationTimeOutMessageSendCancel(taskCooperation, currentTime);
    }

    @Override
    public void delete(Long id) {
        // 获取并校验协同状态是否是已撤销状态
        exceptionTaskCooperationService.validateStatusCancel(id);
        exceptionTaskCooperationService.deleteById(id);
    }

}

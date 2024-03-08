package com.framework.emt.system.domain.task.handing.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cola.extension.Extension;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.framework.admin.system.dao.IUserDao;
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
import com.framework.emt.system.domain.reportnoticeprocess.ReportNoticeProcess;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessService;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessUserService;
import com.framework.emt.system.domain.task.check.service.TaskCheckServiceExtPt;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.handing.request.*;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.code.ExceptionTaskHandingErrorCode;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import com.framework.emt.system.infrastructure.exception.task.handing.service.ExceptionTaskHandingService;
import com.framework.emt.system.infrastructure.exception.task.record.constant.constant.TaskRecordTemplate;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.response.service.ExceptionTaskResponseService;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import com.framework.emt.system.infrastructure.task.conf.TaskConf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常处理节点服务
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@Extension(bizId = "handing")
@RequiredArgsConstructor
@Validated
public class TaskHandingServiceImpl implements TaskHandingServiceExtPt {

    private final ExceptionTaskHandingService exceptionTaskHandingService;

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskSubmitService taskSubmitService;

    private final ExceptionTaskResponseService exceptionTaskResponseService;

    private final UserService userService;

    private final ExceptionTaskRecordService exceptionTaskRecordService;

    private final ExceptionTaskCooperationService exceptionTaskCooperationService;

    private final ReportNoticeProcessService reportNoticeProcessService;

    private final ExceptionTaskSettingService settingService;

    private final ExceptionTaskCooperationService cooperationService;

    private final TaskCheckServiceExtPt checkService;

    private final MessagePushService messagePushService;

    private final IMessageService messageService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    private final ExceptionItemService exceptionItemService;

    private final ReportNoticeProcessUserService reportNoticeProcessUserService;

    private final ExceptionTaskSettingService exceptionTaskSettingService;

    private final IUserDao userDao;

    private final TaskConf taskConf;

    private final IFormFieldUseService formFieldUseService;

    @Override
    @DSTransactional
    public synchronized void reject(@NonNull Long exceptionHandingId, HandingRejectRequest rejectRequest) {
        TaskRecordNode taskRecordNode = TaskRecordNode.getHandingRejectNode(rejectRequest.getRejectNode());
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 异常处理信息 校验当前用户是否异常处理人
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.info(exceptionHandingId, user.getUserId());
        // 异常任务 是否可处理驳回
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusHandingReject(exceptionTaskHanding.getExceptionTaskId());
        boolean isSubmitNode = taskRecordNode.equals(TaskRecordNode.SUBMIT);
        // 驳回到提报节点
        if (isSubmitNode) {
            rejectToSubmitNode(user, exceptionTask, exceptionTaskHanding, rejectRequest.getRejectReason());
            return;
        }
        // 驳回到响应节点
        rejectToResponseNode(user, exceptionTask, exceptionTaskHanding, rejectRequest.getRejectReason());
    }

    @Override
    @DSTransactional
    public synchronized void toOther(@NonNull Long exceptionHandingId, @NonNull Long otherUserId, @NonNull String otherRemark) {
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 验证处理转派人不能设置成自己后获取转派人信息
        User otherUser = userService.userIdConsistent(otherUserId, user.getUserId(),
                ExceptionTaskHandingErrorCode.THE_TRANSFEROR_CANNOT_BE_SET_AS_THEMSELVES,
                ExceptionTaskHandingErrorCode.OTHER_USER_NOT_EXIST);
        // 异常处理信息 校验当前用户是否异常处理人
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.info(exceptionHandingId, user.getUserId());

        updateToOther(exceptionTaskHanding, otherUser, otherRemark, user);
    }

    @Override
    public void adminToOther(Long taskId, Long otherUserId, String otherRemark) {
        //判断是否有转派权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_HANDLE_AND_TRANSFER_EXCEPTIONS);
        }
        // 异常处理信息
        ExceptionTask task = exceptionTaskService.findByIdThrowErr(taskId);
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.findUniqueInfo(taskId, task.getHandingVersion());

        // 验证处理转派人和当前处理人不相同后获取转派人信息
        User otherUser = userService.userIdConsistent(otherUserId, exceptionTaskHanding.getUserId(), ExceptionTaskHandingErrorCode.PROCESSING_TRANSFER_PERSON_CANNOT_BE_THE_SAME_AS_THE_CURRENT_PROCESSING_PERSON, ExceptionTaskHandingErrorCode.OTHER_USER_NOT_EXIST);
        // 当前用户
        FtUser user = AuthUtil.getUser();
        updateToOther(exceptionTaskHanding, otherUser, otherRemark, user);
    }

    /**
     * 更新转派信息
     *
     * @param exceptionTaskHanding 处理id
     * @param otherUser            转派用户信息
     * @param otherRemark          转派备注
     */
    private void updateToOther(ExceptionTaskHanding exceptionTaskHanding, User otherUser, String otherRemark, FtUser user) {
        Long oldUserId = exceptionTaskHanding.getUserId();
        // 异常任务 是否可处理转派
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusHandingToOther(exceptionTaskHanding.getExceptionTaskId());
        // 获取异常提报
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());

        // 异常处理转派
        LocalDateTime otherTime = LocalDateTime.now();
        exceptionTaskHanding.toOtherUser(otherUser.getId(), otherRemark, otherTime);
        exceptionTaskHandingService.update(exceptionTaskHanding);
        // 异常任务转派更新
        exceptionTaskService.update(exceptionTask.handingToOther());

        // 取消其他人响应超时任务
        List<Long> userIds = exceptionTaskHandingService.getUserIds(exceptionTask.getId(), exceptionTaskHanding.getId(), exceptionTask.getHandingVersion());
        if (ObjectUtil.isNotEmpty(userIds)) {
            exceptionTaskScheduleService.handingTimeOutMessageSendCancelByReceive(taskSubmit, otherTime, userIds);
            //删除多余的处理数据
            exceptionTaskHandingService.deleteHanding(exceptionTask.getId(), exceptionTaskHanding.getId(), exceptionTask.getHandingVersion());
        }

        // 新增履历
        exceptionTaskRecordService.handingNodeToOther(user, exceptionTask, exceptionTaskHanding.getId(), otherUser, otherRemark, exceptionTaskHanding);

        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.handingToOtherSend(user, exceptionTask.getId(), itemTitle, otherUser.getId(), exceptionTaskHanding.getId());

        // 修改消息接收人
        // 获取异常配置
        ExceptionTaskSetting exceptionTaskSetting = settingService.info(exceptionTask.getExceptionTaskSettingId());
        if (!ObjectUtil.equal(exceptionTaskSetting.getHandingMode(), HandingModeEnum.MULTIPLE_PERSONNEL)) {
            Integer businessType = ExceptionType.HANDING.getCode();
            messageService.updateReceiveUser(businessType, taskSubmit.getId(), otherUser.getId(), null);
            // 修改异常任务定时计划接收人
            TimeOutType timeOutType = TimeOutType.HANDING;
            exceptionTaskScheduleService.updateAcceptUser(timeOutType, taskSubmit.getId(), otherUser.getId(), null);
        } else {
            Integer businessType = ExceptionType.HANDING.getCode();
            messageService.updateReceiveUser(businessType, taskSubmit.getId(), otherUser.getId(), oldUserId);

            // 修改异常任务定时计划接收人
            TimeOutType timeOutType = TimeOutType.HANDING;
            exceptionTaskScheduleService.updateAcceptUser(timeOutType, taskSubmit.getId(), otherUser.getId(), oldUserId);
        }
    }

    @Override
    @DSTransactional
    public synchronized void accept(@NonNull Long exceptionHandingId) {
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 异常处理信息 校验当前用户是否异常处理人
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.info(exceptionHandingId, user.getUserId());
        // 异常任务 是否可处理转派
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusHandingToAccept(exceptionTaskHanding.getExceptionTaskId());
        // 获取异常响应信息
        ExceptionTaskResponse exceptionTaskResponse = exceptionTaskResponseService.infoByTask(exceptionTask.getId(), exceptionTask.getResponseVersion());
        exceptionTaskResponseService.update(exceptionTaskResponse.handingAccept(user.getUserId()));
        // 获取异常提报下的异常项名称
        String itemTitle = exceptionItemService.findItemName(exceptionTask.getId(), exceptionTask.getSubmitVersion());

        // 异常处理接受
        LocalDateTime acceptTime = LocalDateTime.now();
        exceptionTaskHanding.accept(user.getUserId(), acceptTime);
        exceptionTaskHandingService.update(exceptionTaskHanding);
        // 异常任务接受
        exceptionTaskService.update(exceptionTask.handingAccept());
        // 新增履历
        exceptionTaskRecordService.handingNodeAccept(user, exceptionTask, exceptionHandingId, exceptionTaskHanding);
        ExceptionTaskSetting setting = exceptionTaskSettingService.getById(exceptionTask.getExceptionTaskSettingId());
        if (ObjectUtil.equal(setting.getHandingMode(), HandingModeEnum.MULTIPLE_PERSONNEL)) {
            ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
            // 取消其他人响应超时任务
            List<Long> userIds = exceptionTaskHandingService.getUserIds(exceptionTask.getId(), exceptionHandingId, exceptionTask.getHandingVersion());
            if (ObjectUtil.isNotEmpty(userIds)) {
                exceptionTaskScheduleService.handingTimeOutMessageSendCancelByReceive(taskSubmit, acceptTime, userIds);
                //删除多余的处理数据
                exceptionTaskHandingService.deleteHanding(exceptionTask.getId(), exceptionHandingId, exceptionTask.getHandingVersion());
            }
        }
        // 发送消息
        messagePushService.handingAcceptSend(user, exceptionTask.getId(), itemTitle, exceptionTaskResponse.getUserId(), exceptionHandingId);
    }

    @Override
    @DSTransactional
    public synchronized void suspend(@NonNull Long exceptionHandingId, HandingSuspendRequest request) {
        LocalDateTime now = LocalDateTime.now();
        // 申请挂起的秒数
        long suspendSeconds = request.validateTime(now);
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 异常处理信息 校验当前用户是否异常处理人
        ExceptionTaskHanding taskHanding = exceptionTaskHandingService.info(exceptionHandingId, user.getUserId());
        // 异常任务 是否可以挂起
        ExceptionTask exceptionTask = exceptionTaskService.validateHandingSuspend(taskHanding.getExceptionTaskId(), suspendSeconds);
        // 获取异常提报信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 获取异常项名称
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        // 获取逐级上报人列表
        List<Long> reportUserIds = reportNoticeProcessUserService.findUserIdsByProcessId(taskSubmit.getExceptionProcessId());

        // 验证提报附加字段
        List<FormFieldResponse> suspendExtendData = settingService.validateExtendFields(taskHanding.getSuspendExtendDatas(), request.getSuspendExtendDatas());

        // 异常处理挂起
        taskHanding.suspend(request.getReason(), now, request.getResumeTime(), request.getSuspendFiles(), suspendExtendData);
        exceptionTaskHandingService.update(taskHanding);
        // 异常任务挂起
        exceptionTaskService.update(exceptionTask.handingSuspend());
        // 新增履历
        exceptionTaskRecordService.handingNodeSuspend(user, exceptionTask, taskHanding);

        //定时任务  添加定时  自动恢复
        taskConf.updateSuspend(request.getResumeTime(), Convert.toStr(exceptionHandingId));

        // 发送消息
        messagePushService.handingSuspendSend(user, exceptionTask.getId(), itemTitle, reportUserIds, exceptionHandingId);
        // 取消发送处理、处理上报超时定时消息
        exceptionTaskScheduleService.handingTimeOutMessageSendCancel(taskSubmit, now);

    }

    public void updateSuspendByHanding(Long id) {
        ExceptionTaskHanding taskHanding = exceptionTaskHandingService.getById(id);
        if (!ObjectUtil.equal(taskHanding.getResumeType(), TaskResumeType.HAND)) {
            LocalDateTime now = LocalDateTime.now();
            long suspendSeconds = taskHanding.updateSuspendByHanding(now);
            // 异常任务是否挂起状态
            ExceptionTask exceptionTask = exceptionTaskService.validateHandingResume(taskHanding.getExceptionTaskId());
            // 异常任务挂起恢复
            exceptionTaskService.update(exceptionTask.handingResume(suspendSeconds));
            // 新增履历
            exceptionTaskRecordService.handingNodeSuspendResume(null, false, exceptionTask, taskHanding);
            exceptionTaskHandingService.update(taskHanding);
            // 获取异常提报信息
            ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
            // 获取异常项名称
            String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
            // 发送挂起自动恢复定时消息
            exceptionTaskScheduleService.resumeScheduleMessageSend(false, exceptionTask, taskHanding, itemTitle);
            // 根据当前时间创建处理超时、处理超时上报定时消息并发送
            exceptionTaskScheduleService.handingScheduleMessageSend(taskSubmit, now);
        }

    }

    @Override
    @DSTransactional
    public synchronized void suspendDelay(@NonNull Long exceptionHandingId, HandingSuspendDelayRequest request) {
        LocalDateTime now = LocalDateTime.now();
        // 申请挂起延期的秒数
        long suspendDelaySeconds = request.validateTime(now);
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 异常处理信息 校验当前用户是否异常处理人
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.info(exceptionHandingId, user.getUserId());
        // 异常处理预计挂起的总秒数
        long suspendSeconds = exceptionTaskHanding.getSuspendMinutes();
        // 异常任务 是否可以挂起延期
        ExceptionTask exceptionTask = exceptionTaskService.validateHandingSuspendDelay(exceptionTaskHanding.getExceptionTaskId(), suspendSeconds, suspendDelaySeconds);
        // 异常处理挂起延期
        exceptionTaskHanding.suspendDelay(request.getResumeTime());
        exceptionTaskHandingService.update(exceptionTaskHanding);
        // 新增履历
        exceptionTaskRecordService.handingNodeSuspendDelay(user, exceptionTask, exceptionTaskHanding, request.getReason(), request.getResumeTime());
        //取消定时任务
        taskConf.cancelSuspend(Convert.toStr(exceptionHandingId));
        //新增新的定时任务  定时任务  添加定时  自动恢复
        taskConf.updateSuspend(request.getResumeTime(), Convert.toStr(exceptionHandingId));
    }

    @Override
    @DSTransactional
    public synchronized void resume(@NonNull Long exceptionHandingId, @NonNull TaskResumeType resumeType) {
        Assert.isTrue(!TaskResumeType.INIT.equals(resumeType), "挂起恢复方式错误");
        // 是否手动恢复
        boolean isHandResume = resumeType.equals(TaskResumeType.HAND);
        // 当前用户
        FtUser user = AuthUtil.getUser();
        // 手动恢复 获取当前用户id
        Long userId = resumeType.equals(TaskResumeType.HAND) ? user.getUserId() : null;
        // 异常处理信息 手动恢复：校验当前用户是否异常处理人 自动恢复：系统定时任务恢复
        ExceptionTaskHanding taskHanding = exceptionTaskHandingService.info(exceptionHandingId, userId);
        // 异常任务是否挂起状态
        ExceptionTask exceptionTask = exceptionTaskService.validateHandingResume(taskHanding.getExceptionTaskId());
        // 获取异常提报信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 获取异常项名称
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());

        // 异常处理挂起恢复 获取当前挂起实际时间
        LocalDateTime now = LocalDateTime.now();
        long suspendSeconds = taskHanding.resume(resumeType, now);
        exceptionTaskHandingService.update(taskHanding);
        // 异常任务挂起恢复
        exceptionTaskService.update(exceptionTask.handingResume(suspendSeconds));
        // 新增履历
        exceptionTaskRecordService.handingNodeSuspendResume(isHandResume ? user : null, isHandResume, exceptionTask, taskHanding);

        // 发送挂起自动恢复定时消息
        exceptionTaskScheduleService.resumeScheduleMessageSend(isHandResume, exceptionTask, taskHanding, itemTitle);
        // 根据当前时间创建处理超时、处理超时上报定时消息并发送
        exceptionTaskScheduleService.handingScheduleMessageSend(taskSubmit, isHandResume ? now : exceptionTask.getHandingDeadline());
    }

    @Override
    @DSTransactional
    public synchronized void createCooperation(TaskCooperationCreateRequest request, FtUser user) {
        // 验证协同数据是否超过协同任务最大数目
        cooperationService.validExceedMaxVal(request.getExceptionTaskHandingId());
        // 校验超时上报流程是否存在
        ReportNoticeProcess reportNoticeProcess = reportNoticeProcessService.findByIdThrowErr(request.getReportNoticeProcessId());
        // 协同人是否存在
        User planUser = userService.findUserByIdThrowErr(request.getPlanUserId(), ExceptionTaskHandingErrorCode.PLAN_COOPERATION_USER_NOT_EXIST);
        //验证协同人的数据是否已存在
        cooperationService.validIsExist(request.getExceptionTaskHandingId(), request.getPlanUserId());
        // 校验并获取异常任务处理 并判断协同人是否已有该协同任务
        ExceptionTaskHanding taskHanding = exceptionTaskHandingService.info(request.getExceptionTaskHandingId(), user.getUserId());
        // 异常任务是否可设置协同任务
        ExceptionTask task = exceptionTaskService.validateHandingCooperationTask(taskHanding.getExceptionTaskId());
        // 校验并获取异常任务配置
        ExceptionTaskSetting taskSetting = settingService.findByIdThrowErr(task.getExceptionTaskSettingId());
        // 获取异常提报下的异常项名称
        String itemTitle = exceptionItemService.findItemName(task.getId(), task.getSubmitVersion());

        // 添加异常协同
        LocalDateTime currentTime = LocalDateTime.now();
        String reportName = reportNoticeProcess.getName();
        List<FormFieldResponse> cooperateExtendFields = taskSetting.getCooperateExtendFields();
        ExceptionTaskCooperation taskCooperation = exceptionTaskCooperationService.create(request, taskHanding, currentTime, reportName, cooperateExtendFields);
        // 添加履历
        exceptionTaskRecordService.handingNodeCooperationCreate(user, task, taskCooperation.getId(), planUser.getName(), taskCooperation.getCreateTime(), cooperateExtendFields);

        // 发送消息
        messagePushService.handingCreateCooperationSend(user, task.getId(), itemTitle, request.getPlanUserId(), taskCooperation.getId());
        // 发送协同超时、协同超时上报定时消息
        exceptionTaskScheduleService.cooperationScheduleMessageSend(task, taskCooperation, planUser, itemTitle);
    }

    @Override
    public void administratorUpdate(Long exceptionHandingId, HandingUpdateRequest request) {
        //判断是否有处理更新权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_UPDATE_EXCEPTION_MANAGEMENT_PROCESSING);
        }
        //查询处理信息
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.findByIdThrowErr(exceptionHandingId);
        //判断状态是否是处理中
        exceptionTaskService.validationProcessing(exceptionTaskHanding.getExceptionTaskId());
        //判断异常项是否在配置表的异常原因项列表中
        ExceptionTask exceptionTask = exceptionTaskService.getById(exceptionTaskHanding.getExceptionTaskId());
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = settingService.validateExtendFields(exceptionTaskHanding.getExtendDatas(), request.getSubmitExtendDatas());
        // 更新处理表信息
        exceptionTaskHanding.update(request, exceptionTaskHanding.getUserId(), extendDataList);
        exceptionTaskHandingService.update(exceptionTaskHanding);
        // 新增履历
        exceptionTaskRecordService.handingUpdate(AuthUtil.getUser(), exceptionTask, TaskRecordNode.RESPONSE, exceptionTaskHanding, TaskRecordTemplate.PROCESSING_SUBMISSION_PROCESSING_BY_ADMIN);

    }

    @Override
    public void administratorSubmit(Long exceptionHandingId, HandingSubmitRequest request) {
        //判断是否有处理更新权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(ExceptionTaskHandingErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_UPDATE_EXCEPTION_MANAGEMENT_PROCESSING);
        }
        // 异常处理是否当前用户，且是否可以提交
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.findByIdThrowErr(exceptionHandingId);

        // 判断状态是否是处理中
        ExceptionTask exceptionTask = exceptionTaskService.validationProcessing(exceptionTaskHanding.getExceptionTaskId());
        User user = userDao.getById(exceptionTaskHanding.getUserId());
        FtUser ftUser = new FtUser();
        ftUser.setUserId(user.getId());
        ftUser.setUserName(user.getName());
        updateSubmit(ftUser, exceptionHandingId, request, exceptionTaskHanding, exceptionTask);
        // 新增履历
        exceptionTaskRecordService.handingApplicationCheck(AuthUtil.getUser(), exceptionTask, TaskRecordNode.RESPONSE, exceptionTaskHanding, TaskRecordTemplate.PROCESSING_APPLICATION_ACCEPTANCE_BY_ADMIN);
    }

    /**
     * 公共部分---用于提交处理
     *
     * @param exceptionHandingId   处理id
     * @param request              参数
     * @param exceptionTaskHanding 处理信息
     * @param exceptionTask        任务信息
     */
    private void updateSubmit(FtUser user, Long exceptionHandingId, HandingSubmitRequest request, ExceptionTaskHanding exceptionTaskHanding, ExceptionTask exceptionTask) {

        // 获取异常提报信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 获取、判断验收人列表
        ExceptionTaskSetting setting = settingService.getById(exceptionTask.getExceptionTaskSettingId());
        List<Long> checkUserIds = settingService.validateCheckUser(setting, request.getCheckUserIds(), taskSubmit);
        // 获取协同任务数目
        Long count = cooperationService.count(exceptionHandingId, exceptionTaskHanding.getHandingVersion(), null);
        if (count > 0) {
            Long cooperationCount = cooperationService.count(exceptionHandingId, exceptionTaskHanding.getHandingVersion(), CooperationStatus.ALREADY_COMPLETED);
            if (!ObjectUtil.equal(cooperationCount, count)) {
                throw new ServiceException(ExceptionTaskHandingErrorCode.COLLABORATION_TASKS_NOT_ALL_FINISHED);
            }
        }
        // 异常处理更新提交时间，获取处理总耗时
        LocalDateTime currentTime = LocalDateTime.now();
        Long handingSecond = exceptionTaskHanding.submit(currentTime);
        exceptionTaskHandingService.update(exceptionTaskHanding);
        //任务状态改为待验收
        exceptionTask.handingSubmit(handingSecond, checkUserIds.size());
        exceptionTaskService.update(exceptionTask);
        //创建验收信息
        checkService.create(exceptionTaskHanding.getExceptionTaskId(), exceptionTask.getCheckVersion(), checkUserIds, setting.getCheckExtendFields());
        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.handingSubmitSend(user, exceptionTask.getId(), itemTitle, checkUserIds, exceptionHandingId);
        // 取消发送处理、处理上报超时定时消息
        exceptionTaskScheduleService.handingTimeOutMessageSendCancel(taskSubmit, currentTime);
    }

    /**
     * 驳回到提报节点
     *
     * @param user                 驳回人
     * @param exceptionTask        异常任务
     * @param exceptionTaskHanding 异常处理
     * @param rejectReason         驳回原因
     */
    private void rejectToSubmitNode(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding, String rejectReason) {
        //  获取提报表信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 异常任务表 更新异常状态 提报已驳回
        exceptionTaskService.update(exceptionTask.handingReject(true));

        //  创建提报信息
        LocalDateTime currentTime = LocalDateTime.now();
        TaskRejectRequest taskRejectRequest = new TaskRejectRequest(TaskRejectNode.HANDING, exceptionTaskHanding.getId(), currentTime, rejectReason, user.getUserId());
        taskSubmitService.copy(taskSubmit, taskRejectRequest, exceptionTask.getSubmitVersion());
        // 新增履历
        exceptionTaskRecordService.handingNodeReject(user, exceptionTask, TaskRecordNode.SUBMIT, exceptionTaskHanding.getId(), taskSubmit.getUserId(), rejectReason, currentTime);
        // TODO 推送消息给提报人、定时任务终止

        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.handingRejectToSubmitNodeSend(user, exceptionTask.getId(), itemTitle, taskSubmit.getSubmitUserId(), exceptionTaskHanding.getId());

        // 取消发送超时定时消息
        exceptionTaskScheduleService.handingTimeOutMessageSendCancel(taskSubmit, currentTime);
    }

    /**
     * 驳回到响应节点
     *
     * @param user          驳回人
     * @param exceptionTask 异常任务
     * @param taskHanding   异常处理
     * @param rejectReason  驳回原因
     */
    private void rejectToResponseNode(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding taskHanding, String rejectReason) {
        //  获取提报表信息
        ExceptionTaskSubmit taskSubmit = taskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 获取异常响应信息
        ExceptionTaskResponse taskResponse = exceptionTaskResponseService.infoByTask(exceptionTask.getId(), exceptionTask.getResponseVersion());
        // 异常任务表 更新异常状态 提报已驳回
        exceptionTaskService.update(exceptionTask.handingReject(false));
        //复制出新的响应数据
        LocalDateTime currentTime = LocalDateTime.now();
        TaskRejectRequest taskRejectRequest = new TaskRejectRequest(TaskRejectNode.HANDING, taskHanding.getId(), currentTime, rejectReason, user.getUserId());
        exceptionTaskResponseService.copy(taskResponse, taskRejectRequest, exceptionTask.getResponseVersion(), exceptionTask.getResponseRejectNum());
        // 新增履历
        exceptionTaskRecordService.handingNodeReject(user, exceptionTask, TaskRecordNode.RESPONSE, taskHanding.getId(), taskResponse.getUserId(), rejectReason, currentTime);

        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.handingRejectToResponseNodeSend(user, exceptionTask.getId(), itemTitle, taskResponse.getUserId(), taskHanding.getId());
        // 根据当前时间创建响应超时、响应超时上报定时消息并发送
        exceptionTaskScheduleService.responseScheduleMessageSend(taskSubmit, currentTime);
    }

    @Override
    @DSTransactional
    public synchronized void update(@NonNull Long exceptionHandingId, HandingUpdateRequest request) {
        Long userId = AuthUtil.getUserId();
        //判断处理人是否是当前用户
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.info(exceptionHandingId, userId);
        //判断状态是否是处理中
        exceptionTaskService.validationProcessing(exceptionTaskHanding.getExceptionTaskId());
        //判断异常项是否在配置表的异常原因项列表中
        ExceptionTask exceptionTask = exceptionTaskService.getById(exceptionTaskHanding.getExceptionTaskId());
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = settingService.validateExtendFields(exceptionTaskHanding.getExtendDatas(), request.getSubmitExtendDatas());
        // 更新处理表信息
        exceptionTaskHanding.update(request, userId, extendDataList);
        exceptionTaskHandingService.update(exceptionTaskHanding);
        //添加异常单字段使用信息
        formFieldUseService.createFormFieldUser(extendDataList, exceptionTaskHanding.getId(), BusinessTypeEnum.EXCEPTION_HANDING);
        // 新增履历
        exceptionTaskRecordService.handingUpdate(AuthUtil.getUser(), exceptionTask, TaskRecordNode.RESPONSE, exceptionTaskHanding, TaskRecordTemplate.PROCESSING_SUBMISSION_PROCESSING);
    }

    @Override
    @DSTransactional
    public synchronized void submit(@NonNull Long exceptionHandingId, HandingSubmitRequest request) {
        FtUser user = AuthUtil.getUser();
        // 异常处理是否当前用户，且是否可以提交
        ExceptionTaskHanding exceptionTaskHanding = exceptionTaskHandingService.validateSubmit(exceptionHandingId, user.getUserId());
        // 判断状态是否是处理中
        ExceptionTask exceptionTask = exceptionTaskService.validationProcessing(exceptionTaskHanding.getExceptionTaskId());
        updateSubmit(user, exceptionHandingId, request, exceptionTaskHanding, exceptionTask);
        // 新增履历
        exceptionTaskRecordService.handingApplicationCheck(AuthUtil.getUser(), exceptionTask, TaskRecordNode.RESPONSE, exceptionTaskHanding, TaskRecordTemplate.PROCESSING_APPLICATION_ACCEPTANCE);
    }

}

package com.framework.emt.system.domain.task.check.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cola.extension.Extension;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.framework.admin.system.dao.IUserDao;
import com.framework.admin.system.entity.User;
import com.framework.common.api.exception.ServiceException;
import com.framework.common.auth.entity.FtUser;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.emt.system.domain.exception.convert.constant.enums.CheckConditionEnum;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.messages.service.MessagePushService;
import com.framework.emt.system.domain.task.check.request.TaskCheckCreateRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckRejectRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckRequest;
import com.framework.emt.system.domain.task.check.request.TaskCheckUpdateRequest;
import com.framework.emt.system.infrastructure.common.request.TaskRejectRequest;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.check.constant.code.TaskCheckErrorCode;
import com.framework.emt.system.infrastructure.exception.task.check.service.ExceptionTaskCheckService;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.handing.service.ExceptionTaskHandingService;
import com.framework.emt.system.infrastructure.exception.task.record.constant.constant.TaskRecordTemplate;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 异常验收节点服务
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Slf4j
@Extension(bizId = "check")
@RequiredArgsConstructor
public class TaskCheckServiceImpl implements TaskCheckServiceExtPt {
    private final IMessageService messageService;
    private final ExceptionTaskCheckService exceptionTaskCheckService;
    private final ExceptionTaskService exceptionTaskService;
    private final ExceptionTaskRecordService exceptionTaskRecordService;
    private final ExceptionTaskCheckService taskCheckService;
    private final ExceptionTaskSettingService settingService;
    private final ExceptionTaskHandingService handingService;
    private final MessagePushService messagePushService;
    private final ExceptionTaskSubmitService exceptionTaskSubmitService;
    private final ExceptionTaskScheduleService exceptionTaskScheduleService;
    private final ExceptionItemService exceptionItemService;

    private final IUserDao userDao;


    private final IFormFieldUseService formFieldUseService;

    @Override
    @DSTransactional
    public void create(Long exceptionTaskId, Integer checkVersion, List<Long> userIdList, List<FormFieldResponse> submitExtendDatas) {
        TaskCheckCreateRequest request = new TaskCheckCreateRequest();
        request.checkCreate(exceptionTaskId, checkVersion, userIdList, submitExtendDatas);
        taskCheckService.createCheck(request);
    }

    @Override
    @DSTransactional
    public synchronized Long exceptionCheckPass(FtUser user, Long id, TaskCheckRequest request) {
        // 校验并获取异常验收任务
        ExceptionTaskCheck taskCheck = exceptionTaskCheckService.checkUser(id, user.getUserId());
        // 校验并获取异常任务消息
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusCheck(taskCheck.getExceptionTaskId());
        ExceptionTaskCheck check = exceptionTaskCheckService.getById(id);
        LocalDateTime submitTime = LocalDateTime.now();
        Long result = pass(user, id, request, taskCheck, exceptionTask, submitTime);
        //新增履历
        exceptionTaskRecordService.checkPass(AuthUtil.getUser(), exceptionTask, check, submitTime, TaskRecordTemplate.ACCEPTANCE_PASSED);
        return result;
    }

    @Override
    @DSTransactional
    public synchronized Long exceptionCheckReject(FtUser user, Long id, TaskCheckRejectRequest request) {
        // 获取异常验收
        ExceptionTaskCheck taskCheck = exceptionTaskCheckService.checkUser(id, user.getUserId());
        // 获取异常任务
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusCheck(taskCheck.getExceptionTaskId());
        LocalDateTime rejectTime = LocalDateTime.now();
        Long result = reject(user, id, request, taskCheck, exceptionTask, rejectTime);
        // 新增履历 [操作人]驳回验收信息！驳回原因:{驳回原因}
        exceptionTaskRecordService.checkReject(AuthUtil.getUser(), request.getRejectReason(), exceptionTask, taskCheck, rejectTime, TaskRecordTemplate.ACCEPTANCE_REJECTION);


        return result;
    }

    @Override
    public Long administratorCheckPass(FtUser user, Long id, TaskCheckRequest request) {
        //判断是否有处理更新权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(TaskCheckErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_PASS_EXCEPTION_MANAGEMENT_ACCEPTANCE);
        }
        // 校验并获取异常验收任务
        ExceptionTaskCheck taskCheck = exceptionTaskCheckService.findById(id, null);
        // 校验并获取异常任务消息
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusCheck(taskCheck.getExceptionTaskId());
        ExceptionTaskCheck check = exceptionTaskCheckService.getById(id);
        LocalDateTime submitTime = LocalDateTime.now();
        //获取验收人信息，用于发送消息时使用
        User userInfo = userDao.getById(taskCheck.getUserId());

        FtUser ftUser = new FtUser();
        ftUser.setUserName(userInfo.getName());
        ftUser.setUserId(userInfo.getId());
        Long result = pass(ftUser, id, request, taskCheck, exceptionTask, submitTime);

        //新增履历
        exceptionTaskRecordService.checkPass(AuthUtil.getUser(), exceptionTask, check, submitTime, TaskRecordTemplate.ACCEPTANCE_PASSED_BY_ADMIN);
        return result;
    }

    /**
     * 公用--用于管理员和验收人进行验收通过操作
     *
     * @param user          用户
     * @param id            验收id
     * @param request       参数
     * @param taskCheck     验收信息
     * @param exceptionTask 任务信息
     * @param submitTime    提交验收时间
     * @return
     */

    public Long pass(FtUser user, Long id, TaskCheckRequest request, ExceptionTaskCheck taskCheck, ExceptionTask exceptionTask, LocalDateTime submitTime) {
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = settingService.validateExtendFields(taskCheck.getSubmitExtendDatas(), request.getSubmitExtendDatas());
        // 获取提报信息验收成功通知人
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());

        // 获取处理信息
        ExceptionTaskHanding taskHanding = handingService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getHandingVersion());

        //更新验收信息
        ExceptionTaskSetting setting = settingService.getById(exceptionTask.getExceptionTaskSettingId());
        // 需要多人验收
        TaskCheckUpdateRequest taskCheckUpdateRequest = new TaskCheckUpdateRequest();

        taskCheckUpdateRequest.checkPass(request.getSubmitFiles(), extendDataList, submitTime);
        Long result = taskCheckService.updateCheck(id, taskCheckUpdateRequest);
        if (ObjectUtil.equal(setting.getCheckCondition(), CheckConditionEnum.REQUIRE_EVERYONE_ACCEPTANCE)) {
            //判断是否是多个验收信息，并且所有验收信息状态都是验收通过，是否话更新任务状态为已完成
            Boolean checkResult = exceptionTaskCheckService.AllCheckPass(taskCheck.getExceptionTaskId(), taskCheck.getCheckVersion());
            //更新异常任务表
            if (checkResult) {
                LocalDateTime createTime = LocalDateTime.ofInstant(taskCheck.getCreateTime().toInstant(), ZoneId.systemDefault());
                exceptionTask.checkAllPass(createTime, submitTime);
            } else {
                exceptionTask.checkPassOne();
            }
            exceptionTaskService.updateById(exceptionTask);
        }
        //需要单人验收
        else if (ObjectUtil.equal(setting.getCheckCondition(), CheckConditionEnum.ONLY_ONE_PERSON_IS_REQUIRED_FOR_ACCEPTANCE)) {
            // 更新异常任务表
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createTime = LocalDateTime.ofInstant(taskCheck.getCreateTime().toInstant(), ZoneId.systemDefault());
            exceptionTask.checkAllPass(createTime, submitTime);
            exceptionTaskService.updateById(exceptionTask);
            //更新其他验收信息删除
            exceptionTaskCheckService.deleteByTaskId(exceptionTask.getId(), id, exceptionTask.getCheckVersion());
        }

        // 发送消息
        List<Long> noticeUserIds = taskSubmit.getNoticeUserIds();
        List<Long> handingUserIds = Lists.newArrayList(taskHanding.getUserId());
        List<Long> userIds = DataHandleUtils.mergeElements(noticeUserIds, handingUserIds);
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.checkPassSend(user, exceptionTask.getId(), itemTitle, userIds, id);
        //添加异常单字段使用信息
        formFieldUseService.createFormFieldUser(extendDataList, id, BusinessTypeEnum.EXCEPTION_CHECK);
        return result;
    }

    /**
     * 公用--用于管理员和验收人进行验收驳回操作
     *
     * @param user          用户
     * @param id            验收id
     * @param request       参数
     * @param taskCheck     验收信息
     * @param exceptionTask 任务信息
     * @param rejectTime    验收驳回时间
     * @return
     */
    public Long reject(FtUser user, Long id, TaskCheckRejectRequest request, ExceptionTaskCheck taskCheck, ExceptionTask exceptionTask, LocalDateTime rejectTime) {

        // 获取异常处理
        ExceptionTaskHanding taskHanding = handingService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getHandingVersion());
        // 获取异常提报
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.findUniqueInfo(exceptionTask.getId(), exceptionTask.getSubmitVersion());
        // 更新异常任务表
        exceptionTask.checkReject();
        exceptionTaskService.updateById(exceptionTask);
        //创建处理信息
        TaskRejectRequest taskRejectRequest = new TaskRejectRequest(TaskRejectNode.HANDING, id, rejectTime, request.getRejectReason(), user.getUserId());
        handingService.copy(taskHanding, taskRejectRequest, exceptionTask.getHandingVersion(), exceptionTask.getHandingRejectNum());
        //更新验收信息
        taskCheck.updateCheck(request.getSubmitFiles(), request.getRejectReason());
        Long result = taskCheckService.update(taskCheck);
        taskCheckService.setExpire(taskCheck.getExceptionTaskId(), taskCheck.getId());

        // 发送消息
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        messagePushService.checkRejectSend(user, exceptionTask.getId(), itemTitle, taskHanding.getUserId(), id);
        // 根据当前时间创建处理超时、处理超时上报定时消息并发送
        exceptionTaskScheduleService.handingScheduleMessageSend(taskSubmit, rejectTime);
        return result;
    }

    @Override
    public Long administratorCheckReject(FtUser user, Long id, TaskCheckRejectRequest request) {
        //判断是否有处理更新权限
        Integer exceptionManagement = messageService.getExceptionManagement();
        if (ObjectUtil.equal(exceptionManagement, 0)) {
            throw new ServiceException(TaskCheckErrorCode.THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_REJECT_EXCEPTION_MANAGEMENT_ACCEPTANCE);
        }
        // 获取异常验收
        ExceptionTaskCheck taskCheck = exceptionTaskCheckService.findById(id, null);
        // 获取异常任务
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusCheck(taskCheck.getExceptionTaskId());
        LocalDateTime rejectTime = LocalDateTime.now();
        User userInfo = userDao.getById(taskCheck.getUserId());
        //查询验收人 用于发送消息和创建处理数据时的用户信息
        FtUser ftUser = new FtUser();
        ftUser.setUserName(userInfo.getName());
        ftUser.setUserId(userInfo.getId());
        Long result = reject(ftUser, id, request, taskCheck, exceptionTask, rejectTime);
        // 新增履历 [操作人]驳回验收信息！驳回原因:{驳回原因}
        exceptionTaskRecordService.checkReject(AuthUtil.getUser(), request.getRejectReason(), exceptionTask, taskCheck, rejectTime, TaskRecordTemplate.ACCEPTANCE_REJECTION_BY_ADMIN);
        return result;
    }


}

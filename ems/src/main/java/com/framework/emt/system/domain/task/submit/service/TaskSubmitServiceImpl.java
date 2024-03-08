package com.framework.emt.system.domain.task.submit.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cola.extension.Extension;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.framework.admin.system.entity.User;
import com.framework.common.auth.entity.FtUser;
import com.framework.common.auth.utils.AuthUtil;
import com.framework.emt.system.domain.dept.service.DeptService;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.exception.convert.constant.enums.HandingModeEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.ResponseModeEnum;
import com.framework.emt.system.domain.exception.response.ExceptionProcessResponse;
import com.framework.emt.system.domain.exception.service.ExceptionItemService;
import com.framework.emt.system.domain.exception.service.ExceptionProcessService;
import com.framework.emt.system.domain.formfield.constant.enums.BusinessTypeEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.formfieldUse.service.IFormFieldUseService;
import com.framework.emt.system.domain.messages.service.MessagePushService;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.tag.service.TagService;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitCreateRequest;
import com.framework.emt.system.domain.task.submit.request.TaskSubmitUpdateRequest;
import com.framework.emt.system.domain.user.code.UserErrorCode;
import com.framework.emt.system.domain.user.service.UserService;
import com.framework.emt.system.domain.workspacelocation.service.WorkspaceLocationService;
import com.framework.emt.system.infrastructure.exception.task.record.service.ExceptionTaskRecordService;
import com.framework.emt.system.infrastructure.exception.task.response.service.ExceptionTaskResponseService;
import com.framework.emt.system.infrastructure.exception.task.schedule.service.ExceptionTaskScheduleService;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTaskSetting;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskService;
import com.framework.emt.system.infrastructure.exception.task.task.service.ExceptionTaskSettingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常提报节点服务
 *
 * @author jiaXue
 * date 2023/8/8
 */
@Extension(bizId = "submit")
@RequiredArgsConstructor
public class TaskSubmitServiceImpl implements TaskSubmitServiceExtPt {

    private final ExceptionProcessService exceptionProcessService;

    private final ExceptionItemService exceptionItemService;

    private final DeptService deptService;

    private final WorkspaceLocationService workspaceLocationService;

    private final UserService userService;

    private final ExceptionTaskSettingService exceptionTaskSettingService;

    private final ExceptionTaskSubmitService exceptionTaskSubmitService;

    private final ExceptionTaskRecordService exceptionTaskRecordService;

    private final TagService tagService;

    private final ExceptionTaskService exceptionTaskService;

    private final ExceptionTaskResponseService exceptionTaskResponseService;

    private final MessagePushService messagePushService;

    private final ExceptionTaskScheduleService exceptionTaskScheduleService;

    private final IFormFieldUseService formFieldUseService;

    @Override
    @DSTransactional
    public Long create(TaskSubmitCreateRequest request) {
        // 创建校验
        validateForCreateOrUpdate(request.getDeptId(), request.getWorkspaceId(), request.getNoticeUserIds());
        // 获取异常流程 校验响应人
        ExceptionProcessResponse exceptionProcess = exceptionProcessService.validateResponseUser(request.getExceptionProcessId(), request.getResponseUserId());
        // 获取异常分类id 校验异常分类
        Long exceptionCategoryId = exceptionItemService.validateExceptionCategory(exceptionProcess.getExceptionCategoryId(), request.getExceptionItemId());
        // 获取异常原因项列表
        List<TagInfo> reasonItems = tagService.findTagListBySourceId(exceptionProcess.getId(), SourceTypeEnum.ABNORMAL_PROCESS);
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = exceptionTaskSettingService.validateExtendFields(exceptionProcess.getSubmitExtendFieldList(), request.getSubmitExtendDatas());

        // 添加异常任务配置
        ExceptionTaskSetting exceptionTaskSetting = exceptionTaskSettingService.create(exceptionProcess, reasonItems);
        // 添加异常任务
        ExceptionTask exceptionTask = exceptionTaskService.create(exceptionTaskSetting.getId(), ExceptionStatus.DRAFT, ExceptionSubStatus.DRAFT);
        // 添加异常提报
        ExceptionTaskSubmit exceptionTaskSubmit = exceptionTaskSubmitService.create(AuthUtil.getUserId(), request, exceptionProcess.getId(), exceptionProcess.getTitle(),
                exceptionCategoryId, exceptionTask.getId(), exceptionTask.getSubmitVersion(), extendDataList);
        // 添加履历
        exceptionTaskRecordService.submitNodeCreate(AuthUtil.getUser(), exceptionTask, exceptionTaskSubmit);
        //添加异常单字段使用信息
        formFieldUseService.createFormFieldUser(extendDataList, exceptionTaskSubmit.getId(), BusinessTypeEnum.EXCEPTION_SUBMIT);
        return exceptionTaskSubmit.getId();
    }

    @Override
    @DSTransactional
    public void update(Long id, TaskSubmitUpdateRequest request) {
        // 异常提报创建人是否是当前用户
        ExceptionTaskSubmit exceptionTaskSubmit = exceptionTaskSubmitService.validateUser(id, AuthUtil.getUserId());
        // 异常状态是否可以更新
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusUpdate(exceptionTaskSubmit.getExceptionTaskId());
        // 更新校验
        validateForCreateOrUpdate(request.getDeptId(), request.getWorkspaceId(), request.getNoticeUserIds());
        // 获取异常流程 校验响应人
        ExceptionProcessResponse exceptionProcess = exceptionProcessService.validateResponseUser(request.getExceptionProcessId(), request.getResponseUserId());
        // 获取异常分类id 校验异常分类
        exceptionItemService.validateExceptionCategory(exceptionProcess.getExceptionCategoryId(), request.getExceptionItemId());
        // 获取异常原因项列表
        List<TagInfo> reasonItems = tagService.findTagListBySourceId(exceptionProcess.getId(), SourceTypeEnum.ABNORMAL_PROCESS);
        // 验证提报附加字段
        List<FormFieldResponse> extendDataList = exceptionTaskSettingService.validateExtendFields(exceptionProcess.getSubmitExtendFieldList(), request.getSubmitExtendDatas());

        // 更新异常任务配置
        exceptionTaskSettingService.update(exceptionProcess, exceptionTask.getExceptionTaskSettingId(), reasonItems);
        // 更新异常提报
        exceptionTaskSubmitService.update(exceptionTaskSubmit, request, exceptionProcess, extendDataList);
        // 添加履历
        exceptionTaskRecordService.submitNodeUpdate(AuthUtil.getUser(), exceptionTask, exceptionTaskSubmit);
        //添加异常单字段使用信息
        //删除后新增异常单字段使用信息
        formFieldUseService.deleteByKeyIdAndBusinessType(exceptionTaskSubmit.getId(), BusinessTypeEnum.EXCEPTION_SUBMIT);
        formFieldUseService.createFormFieldUser(extendDataList, exceptionTaskSubmit.getId(), BusinessTypeEnum.EXCEPTION_SUBMIT);
    }

    @Override
    @DSTransactional
    public synchronized void submit(Long id, FtUser user) {
        // 异常提报创建人是否是当前用户
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.validateUser(id, user.getUserId());
        // 异常状态是否可以提报
        ExceptionTask task = exceptionTaskService.validateStatusSubmit(taskSubmit.getExceptionTaskId());
        // 获取异常配置
        ExceptionTaskSetting setting = exceptionTaskSettingService.info(task.getExceptionTaskSettingId());
        // 获取异常流程
        ExceptionProcess process = exceptionProcessService.findReportProcessByIdThrowErr(taskSubmit.getExceptionProcessId());
        // 获取异常提报下的异常项名称
        String itemTitle = exceptionItemService.getTitleById(taskSubmit.getExceptionItemId());
        List<Long> responseUserIds = exceptionTaskSettingService.validateResponseUser(setting, taskSubmit.getSubmitResponseUserId());
        if (ResponseModeEnum.NOT_SPECIFIED.equals(setting.getResponseMode())) {
            setting.setResponseUserIds(responseUserIds);
            exceptionTaskSettingService.update(setting);
        }
        // 提报数据更新
        taskSubmit.submit(LocalDateTime.now(), user.getUserId(), task.getSubmitVersion());
        exceptionTaskSubmitService.update(taskSubmit);
        // 更新异常任务数据
        task.submit(taskSubmit.getResponseDurationTime(), taskSubmit.getHandingDurationTime(), taskSubmit.getSubmitTime());
        exceptionTaskService.update(task);
        // 调用响应创建的接口
        exceptionTaskResponseService.create(task.getId(), task.getResponseVersion(), responseUserIds, setting.getResponseExtendFields());
        List<User> responseUserList = userService.findUsers(responseUserIds);
        // 添加履历
        exceptionTaskRecordService.submitNodeSubmit(AuthUtil.getUser(), task, taskSubmit, responseUserList);
        // 发送通知消息
        messagePushService.submitSend(user, task.getId(), itemTitle, responseUserIds, id);

        // 发送响应超时、响应超时上报定时消息
        if (!ObjectUtil.equal(setting.getResponseMode(), ResponseModeEnum.MULTIPLE_PERSONNEL)) {
            exceptionTaskScheduleService.responseScheduleMessageSend(task, taskSubmit, process, responseUserList.get(0), itemTitle);
        } else {
            exceptionTaskScheduleService.responseScheduleMessageListSend(task, taskSubmit, process, responseUserList, responseUserIds, itemTitle);
        }
        if (!ObjectUtil.equal(setting.getHandingMode(), HandingModeEnum.MULTIPLE_PERSONNEL)) {
            // 发送处理超时、处理超时上报定时消息
            exceptionTaskScheduleService.handingScheduleMessageSend(task, taskSubmit, process, itemTitle);
        } else {
            List<Long> handingUserIds = setting.getHandingUserIds();
            List<User> handingUsers = userService.findUsers(handingUserIds);
            exceptionTaskScheduleService.handingScheduleMessageListSend(task, taskSubmit, process, handingUsers, handingUserIds, itemTitle);
        }
    }

    @Override
    @DSTransactional
    public void close(Long id) {
        // 异常提报是否存在、且创建人是当前用户
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.validateUser(id, AuthUtil.getUserId());
        // 异常任务是否存在、是否可关闭 状态
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusClose(taskSubmit.getExceptionTaskId());
        // 修改异常提报对应的异常任务状态
        exceptionTaskService.update(exceptionTask.close(LocalDateTime.now()));
        // 新增一条异常任务履历信息
        exceptionTaskRecordService.submitNodeClose(AuthUtil.getUser(), exceptionTask, taskSubmit);
    }

    @Override
    public void cancel(Long id) {
        // 异常提报是否存在、且创建人是当前用户
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.validateUser(id, AuthUtil.getUserId());
        // 异常任务是否存在、状态是否是待响应状态
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusResponse(taskSubmit.getExceptionTaskId());
        // 修改异常提报对应的异常任务状态
        exceptionTaskService.update(exceptionTask.cancel());
        // 更新提报撤销时间
        LocalDateTime currentTime = LocalDateTime.now();
        exceptionTaskSubmitService.update(taskSubmit.cancel(currentTime));
        // 新增一条异常任务履历信息
        exceptionTaskRecordService.submitNodeCancel(AuthUtil.getUser(), exceptionTask, taskSubmit);

        // 定时消息取消发送
        exceptionTaskScheduleService.timeOutMessageSendCancel(taskSubmit, currentTime);
    }

    @Override
    @DSTransactional
    public void delete(Long id) {
        // 异常提报是否存在、且创建人是当前用户
        ExceptionTaskSubmit taskSubmit = exceptionTaskSubmitService.validateUser(id, AuthUtil.getUserId());
        // 异常任务否存在、且异常提报主状态是待提交
        ExceptionTask exceptionTask = exceptionTaskService.validateStatusDelete(taskSubmit.getExceptionTaskId());
        // 删除异常提报信息
        exceptionTaskSubmitService.deleteById(taskSubmit.getId());
        // 删除异常提报对应的异常任务信息
        exceptionTaskService.deleteById(exceptionTask.getId());
        // 删除异常任务对应的异常任务配置信息
        exceptionTaskSettingService.deleteById(exceptionTask.getExceptionTaskSettingId());
        // 如果是已撤销的提报，删除对应的响应信息
//        if (ExceptionStatus.CANCEL.equals(exceptionTask.getTaskStatus())) {
//            ExceptionTaskResponse exceptionTaskResponse = exceptionTaskResponseService.infoByTask(exceptionTask.getId(), exceptionTask.getResponseVersion());
//            exceptionTaskResponseService.deleteById(exceptionTaskResponse.getId());
//        }
        // 新增履历
        exceptionTaskRecordService.submitNodeDelete(AuthUtil.getUser(), exceptionTask, taskSubmit);
    }

    /**
     * 校验部门、工作单元、提报响应人、成功通知人列表在数据库中都存在
     *
     * @param deptId        部门id
     * @param workspaceId   作业单元id
     * @param noticeUserIds 通知人id列表
     */
    private void validateForCreateOrUpdate(Long deptId, Long workspaceId, List<Long> noticeUserIds) {
        // 校验部门存在
        deptService.findByIdThrowErr(deptId);
        // 校验工作单元存在
        workspaceLocationService.findByIdThrowErr(workspaceId);
        // 校验成功通知人列表
        if (CollectionUtil.isNotEmpty(noticeUserIds)) {
            userService.checkUserExist(noticeUserIds, UserErrorCode.CHECK_RECIPIENT_USER_LIST_CONTAIN_NOT_EXIST_RECIPIENT_USER);
        }
    }

}

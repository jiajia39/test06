package com.framework.emt.system.infrastructure.exception.task.record.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.StrPool;
import com.framework.admin.system.entity.User;
import com.framework.common.auth.entity.FtUser;
import com.framework.common.property.utils.SpringUtil;
import com.framework.core.template.ITemplateService;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.cooperation.service.ExceptionTaskCooperationService;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.record.ExceptionTaskRecord;
import com.framework.emt.system.infrastructure.exception.task.record.constant.constant.TaskRecordTemplate;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import com.framework.emt.system.infrastructure.exception.task.record.mapper.ExceptionTaskRecordMapper;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.schedule.ExceptionTaskSchedule;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitDetailResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.service.ExceptionTaskSubmitService;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 异常任务履历 实现类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExceptionTaskRecordServiceImpl extends BaseServiceImpl<ExceptionTaskRecordMapper, ExceptionTaskRecord> implements ExceptionTaskRecordService {

    private final ITemplateService templateService;

    @Override
    public void submitNodeCreate(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.CREATE);
        exceptionTaskRecord.setSubmitUserId(exceptionTaskSubmit.getCreateUser());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(NumberUtils.INTEGER_ONE);
        exceptionTaskRecord.setRecordFiles(exceptionTaskSubmit.getSubmitFiles());

        LocalDateTime recordTime = LocalDateTime.ofInstant(exceptionTaskSubmit.getCreateTime().toInstant(), ZoneId.systemDefault());
        exceptionTaskRecord.setRecordTime(recordTime);
        List<FormFieldResponse> extendDatas = CollectionUtil.isEmpty(exceptionTaskSubmit.getSubmitExtendDatas()) ? Collections.emptyList() : exceptionTaskSubmit.getSubmitExtendDatas();
        exceptionTaskRecord.setRecordExtendDatas(extendDatas);

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("code", String.valueOf(exceptionTask.getId()));
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_CREATE);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void submitNodeUpdate(FtUser user, ExceptionTask exceptionTask, ExceptionTaskSubmit exceptionTaskSubmit) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.UPDATE);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getSubmitVersion());
        exceptionTaskRecord.setRecordFiles(exceptionTaskSubmit.getSubmitFiles());

        LocalDateTime recordTime = LocalDateTime.ofInstant(exceptionTaskSubmit.getUpdateTime().toInstant(), ZoneId.systemDefault());
        exceptionTaskRecord.setRecordTime(recordTime);
        List<FormFieldResponse> extendDatas = CollectionUtil.isEmpty(exceptionTaskSubmit.getSubmitExtendDatas()) ? Collections.emptyList() : exceptionTaskSubmit.getSubmitExtendDatas();
        exceptionTaskRecord.setRecordExtendDatas(extendDatas);

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_ONE);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_UPDATE);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void submitNodeSubmit(FtUser user, ExceptionTask exceptionTask, ExceptionTaskSubmit exceptionTaskSubmit, List<User> responseUsers) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.SUBMIT);
        exceptionTaskRecord.setSubmitUserId(exceptionTaskSubmit.getSubmitUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getSubmitVersion());
        exceptionTaskRecord.setRecordTime(exceptionTaskSubmit.getSubmitTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());
        String userNames = responseUsers.stream().map(User::getName).collect(Collectors.joining(StrPool.COMMA));
        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("respondent", userNames);
        String content = templateService.render(map, TaskRecordTemplate.REPORTING);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void submitNodeClose(FtUser user, ExceptionTask exceptionTask, ExceptionTaskSubmit exceptionTaskSubmit) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.CLOSE);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getSubmitVersion());
        exceptionTaskRecord.setRecordTime(exceptionTask.getCloseTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_ONE);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.REPORTING_CLOSURE);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void submitNodeCancel(FtUser user, ExceptionTask exceptionTask, ExceptionTaskSubmit exceptionTaskSubmit) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.CANCEL);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getSubmitVersion());
        exceptionTaskRecord.setRecordTime(exceptionTaskSubmit.getCancelTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_ONE);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.REPORTING_CANCEL);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void submitNodeDelete(FtUser user, ExceptionTask exceptionTask, ExceptionTaskSubmit exceptionTaskSubmit) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.SUBMIT, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.DELETE);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskSubmit.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getSubmitVersion());
        exceptionTaskRecord.setRecordTime(LocalDateTime.now());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_ONE);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.REPORT_DELETION);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void responseTransfer(FtUser user, User transferUser, ExceptionTask exceptionTask, Long taskResponseId, LocalDateTime otherTime) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.RESPONSE, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.TRANSFER);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setAcceptUserId(transferUser.getId());
        exceptionTaskRecord.setRecordId(taskResponseId);
        exceptionTaskRecord.setRecordVersion(exceptionTask.getResponseVersion());
        exceptionTaskRecord.setRecordNum(exceptionTask.getResponseOtherCount());
        exceptionTaskRecord.setRecordTime(otherTime);
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        Map<String, String> map = new HashMap<>();
        map.put("operator", user.getUserName());
        map.put("respondent", transferUser.getName());
        String content = templateService.render(map, TaskRecordTemplate.RESPONSE_TRANSFER);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void responseAccept(FtUser user, ExceptionTask task, Long ResponseId, ExceptionTaskResponse taskResponse, LocalDateTime acceptTime) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.RESPONSE, task, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.ACCEPT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(ResponseId);
        exceptionTaskRecord.setRecordVersion(task.getResponseVersion());
        exceptionTaskRecord.setRecordNum(task.getResponseOtherCount());
        exceptionTaskRecord.setRecordTime(acceptTime);
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        Map<String, String> map = new HashMap<>();
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.RESPONSE);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void responseHanding(FtUser user, List<User> submitHandingUsers, ExceptionTask task, Long taskResponseId, ExceptionTaskResponse taskResponse) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.RESPONSE, task, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.SUBMIT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(taskResponseId);
        exceptionTaskRecord.setRecordVersion(task.getResponseVersion());
        exceptionTaskRecord.setRecordNum(task.getResponseOtherCount());
        exceptionTaskRecord.setRecordTime(taskResponse.getSubmitTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());
        String userNames = submitHandingUsers.stream().map(User::getName).collect(Collectors.joining(StrPool.COMMA));
        Map<String, String> map = new HashMap<>();
        map.put("operator", user.getUserName());
        map.put("processed", userNames);
        String content = templateService.render(map, TaskRecordTemplate.RESPONSE_SUBMISSION_RESPONSE);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void responseNodeReject(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull Long taskResponseId,
                                   @NonNull ExceptionTaskSubmit exceptionTaskSubmit, LocalDateTime currentTime, String rejectReason) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.RESPONSE, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.REJECT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(taskResponseId);
        exceptionTaskRecord.setRecordVersion(exceptionTask.getResponseVersion());
        exceptionTaskRecord.setAcceptUserId(exceptionTaskSubmit.getSubmitUserId());
        exceptionTaskRecord.setRecordRemark(rejectReason);
        exceptionTaskRecord.setRecordTime(currentTime);
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("rejectReason", exceptionTaskSubmit.getRejectReason());
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_RESPONSE_REJECT);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeReject(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, Long taskHandingId,
                                  Long acceptUserId, String rejectReason, LocalDateTime rejectTime) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.REJECT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(taskHandingId);
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setAcceptUserId(acceptUserId);
        exceptionTaskRecord.setRecordTime(rejectTime);
        exceptionTaskRecord.setRecordRemark(rejectReason);
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        map.put("node", taskRecordNode.getName());
        map.put("rejectReason", rejectReason);
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_REJECT);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingUpdate(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, ExceptionTaskHanding exceptionTaskHanding, String recordTemplate) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.SUBMIT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskHanding.getId());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordFiles(exceptionTaskHanding.getFiles());

        LocalDateTime recordTime = LocalDateTime.ofInstant(exceptionTaskHanding.getUpdateTime().toInstant(), ZoneId.systemDefault());
        exceptionTaskRecord.setRecordTime(recordTime);
        List<FormFieldResponse> extendDatas = CollectionUtil.isEmpty(exceptionTaskHanding.getExtendDatas()) ? Collections.emptyList() : exceptionTaskHanding.getExtendDatas();
        exceptionTaskRecord.setRecordExtendDatas(extendDatas);

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, recordTemplate);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingApplicationCheck(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, ExceptionTaskHanding exceptionTaskHanding, String recordTemplate) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.CREATE);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskHanding.getId());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordTime(exceptionTaskHanding.getSubmitTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, recordTemplate);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeToOther(FtUser user, ExceptionTask exceptionTask, Long taskHandingId, User otherUser, String otherRemark, ExceptionTaskHanding exceptionTaskHanding) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.TRANSFER);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(taskHandingId);
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordNum(exceptionTask.getHandingOtherCount());
        exceptionTaskRecord.setAcceptUserId(otherUser.getId());
        exceptionTaskRecord.setRecordRemark(otherRemark);
        exceptionTaskRecord.setRecordTime(exceptionTaskHanding.getOtherTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(2);
        map.put("operator", user.getUserName());
        map.put("otherUser", otherUser.getName());
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_TO_OTHER);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);

    }

    @Override
    public void handingNodeAccept(FtUser user, ExceptionTask exceptionTask, Long taskHandingId, ExceptionTaskHanding exceptionTaskHanding) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.ACCEPT);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(taskHandingId);
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordNum(exceptionTask.getHandingOtherCount());
        exceptionTaskRecord.setRecordTime(exceptionTaskHanding.getAcceptTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(1);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_ACCEPT);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeSuspend(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.SUSPEND);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskHanding.getId());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordNum(exceptionTaskHanding.getSuspendNum());
        exceptionTaskRecord.setRecordRemark(exceptionTaskHanding.getSuspendReason());
        exceptionTaskRecord.setRecordTime(exceptionTaskHanding.getSuspendTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(exceptionTaskHanding.getSuspendFiles());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        map.put("reason", exceptionTaskHanding.getSuspendReason());
        map.put("time", LocalDateTimeUtil.format(exceptionTaskHanding.getResumeTime(), DatePattern.NORM_DATETIME_PATTERN));
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_SUSPEND);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeSuspendDelay(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding, String reason, LocalDateTime resumeTime) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.SUSPEND_DELAY);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(exceptionTaskHanding.getId());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordNum(exceptionTaskHanding.getSuspendNum());
        exceptionTaskRecord.setRecordRemark(reason);
        exceptionTaskRecord.setRecordTime(resumeTime);
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        map.put("reason", reason);
        map.put("time", LocalDateTimeUtil.format(exceptionTaskHanding.getResumeTime(), DatePattern.NORM_DATETIME_PATTERN));
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_SUSPEND_DELAY);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeSuspendResume(FtUser user, @NonNull boolean isHandResume, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskHanding exceptionTaskHanding) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(isHandResume ? TaskRecordType.RESUME_HAND : TaskRecordType.RESUME_AUTO);
        exceptionTaskRecord.setSubmitUserId(isHandResume ? user.getUserId() : 0);
        exceptionTaskRecord.setRecordId(exceptionTaskHanding.getId());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordNum(exceptionTaskHanding.getSuspendNum());
        exceptionTaskRecord.setRecordTime(exceptionTaskHanding.getResumeRealTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        if (isHandResume) {
            Map<String, String> map = new HashMap<>(1);
            map.put("operator", user.getUserName());
            String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_HAND_RESUME);
            exceptionTaskRecord.setRecordContent(content);
            create(exceptionTaskRecord);
            return;
        }
        String content = TaskRecordTemplate.EXCEPTION_TASK_HANDING_AUTO_RESUME;
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void handingNodeCooperationCreate(FtUser user, ExceptionTask exceptionTask, Long recordId, String planUserName,
                                             Date createTime, List<FormFieldResponse> cooperateExtendFields) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, true);
        exceptionTaskRecord.setRecordType(TaskRecordType.COLLABORATIVE_SUBMISSION_PROCESSING);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordId(recordId);
        exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTask.getHandingVersion());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        LocalDateTime recordTime = LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
        exceptionTaskRecord.setRecordTime(recordTime);
        List<FormFieldResponse> extendDatas = CollectionUtil.isEmpty(cooperateExtendFields) ? Collections.emptyList() : cooperateExtendFields;
        exceptionTaskRecord.setRecordExtendDatas(extendDatas);

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("cooperationUser", planUserName);
        String content = templateService.render(map, TaskRecordTemplate.PROCESS_SETTINGS_COLLABORATION);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void checkPass(FtUser user, ExceptionTask exceptionTask, ExceptionTaskCheck check, LocalDateTime submitTime, String recordTemplate) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.CHECK, exceptionTask, false);
        exceptionTaskRecord.checkPass(user.getUserId(), check.getId(), exceptionTask.getCheckVersion(), submitTime);

        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        String content = templateService.render(map,recordTemplate);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void checkReject(FtUser user, String rejectReason, ExceptionTask task, ExceptionTaskCheck check, LocalDateTime rejectTime, String recordTemplate) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.CHECK, task, false);
        exceptionTaskRecord.checkReject(user.getUserId(), check.getId(), task.getCheckVersion(), rejectTime,rejectReason);
        // 创建模版内容
        Map<String, String> map = new HashMap<>(3);
        map.put("operator", user.getUserName());
        map.put("reason", rejectReason);
        String content = templateService.render(map,recordTemplate);
        exceptionTaskRecord.setRecordContent(content);
        create(exceptionTaskRecord);
    }

    @Override
    public void cooperationNodeSubmit(FtUser user, ExceptionTask exceptionTask, ExceptionTaskCooperation exceptionTaskCooperation) {
        ExceptionTaskRecord exceptionTaskRecord = initTaskCooperation(exceptionTask, exceptionTaskCooperation);
        exceptionTaskRecord.setRecordType(TaskRecordType.COLLABORATIVE_PROCESSING);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordTime(exceptionTaskCooperation.getSubmitTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.PROCESS_SUBMISSION_COLLABORATION);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void cooperationNodeTransfer(FtUser user, ExceptionTask exceptionTask, ExceptionTaskCooperation exceptionTaskCooperation, User transferUser) {
        ExceptionTaskRecord exceptionTaskRecord = initTaskCooperation(exceptionTask, exceptionTaskCooperation);
        exceptionTaskRecord.setRecordType(TaskRecordType.COLLABORATIVE_TRANSFER);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setAcceptUserId(transferUser.getId());
        exceptionTaskRecord.setRecordTime(exceptionTaskCooperation.getOtherTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("newCollaborator", transferUser.getName());
        String content = templateService.render(map, TaskRecordTemplate.EXCEPTION_TASK_HANDING_COLLABORATION_TRANSFER);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void cooperationNodeCancel(FtUser user, ExceptionTask task, ExceptionTaskCooperation taskCooperation) {
        ExceptionTaskRecord exceptionTaskRecord = initTaskCooperation(task, taskCooperation);
        exceptionTaskRecord.setRecordType(TaskRecordType.CANCEL);
        exceptionTaskRecord.setSubmitUserId(user.getUserId());
        exceptionTaskRecord.setRecordTime(taskCooperation.getCancelTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        map.put("title", taskCooperation.getTitle());
        String content = templateService.render(map, TaskRecordTemplate.COLLABORATION_CANCEL);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void cooperationNodeAccept(FtUser user, ExceptionTask exceptionTask, ExceptionTaskCooperation exceptionTaskCooperation) {
        ExceptionTaskRecord exceptionTaskRecord = initTaskCooperation(exceptionTask, exceptionTaskCooperation);
        exceptionTaskRecord.setRecordType(TaskRecordType.COLLABORATIVE_PROCESSING);
        exceptionTaskRecord.setAcceptUserId(user.getUserId());
        exceptionTaskRecord.setRecordTime(exceptionTaskCooperation.getAcceptTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());

        // 创建模版内容
        Map<String, String> map = new HashMap<>(NumberUtils.INTEGER_TWO);
        map.put("operator", user.getUserName());
        String content = templateService.render(map, TaskRecordTemplate.HANDING_ACCEPTANCE_COLLABORATION);
        exceptionTaskRecord.setRecordContent(content);

        create(exceptionTaskRecord);
    }

    @Override
    public void timeOutMessage(MessageToBeSendResponse sendResponse, TaskRecordNode recordNode, TaskRecordType recordType, Boolean isCooperation) {
        log.info("进入异常定时超时消息记录接口");
        log.info("sendResponse消息待发送数据为：" + sendResponse.toString());
        ExceptionTaskRecord taskRecord = new ExceptionTaskRecord();
        taskRecord.setRecordNode(recordNode);
        taskRecord.setRecordType(recordType);
        taskRecord.setSubmitUserId(sendResponse.getSendUserId());
        taskRecord.setAcceptUserId(sendResponse.getReceiveUserId());
        taskRecord.setRecordContent(sendResponse.getContent());
        taskRecord.setRecordTime(sendResponse.getMessageSendTime());
        taskRecord.setRecordExtendDatas(Collections.emptyList());
        taskRecord.setRecordFiles(Collections.emptyList());

        if (isCooperation) {
            ExceptionTaskCooperationService taskCooperationService = SpringUtil.getBean(ExceptionTaskCooperationService.class);
            ExceptionTaskCooperation taskCooperation = taskCooperationService.getById(sendResponse.getBusinessId());
            taskRecord.setRecordId(taskCooperation == null ? NumberUtils.LONG_ZERO : taskCooperation.getId());
            taskRecord.setRecordStatus(taskCooperation == null ? NumberUtils.INTEGER_ZERO : taskCooperation.getCooperationStatus().getCode());
            taskRecord.setRecordSubStatus(taskCooperation == null ? NumberUtils.INTEGER_ZERO : taskCooperation.getCooperationSubStatus().getCode());
            taskRecord.setRecordVersion(taskCooperation == null ? NumberUtils.INTEGER_ZERO : taskCooperation.getHandingVersion());
            taskRecord.setExceptionTaskId(taskCooperation == null ? NumberUtils.LONG_ZERO : taskCooperation.getExceptionTaskId());
        }

        if (!isCooperation) {
            ExceptionTaskSubmitService taskSubmitService = SpringUtil.getBean(ExceptionTaskSubmitService.class);
            TaskSubmitDetailResponse taskSubmit = taskSubmitService.findDetailById(sendResponse.getBusinessId());
            taskRecord.setRecordId(taskSubmit == null ? NumberUtils.LONG_ZERO : taskSubmit.getId());
            taskRecord.setRecordStatus(taskSubmit == null ? NumberUtils.INTEGER_ZERO : taskSubmit.getStatus());
            taskRecord.setRecordSubStatus(taskSubmit == null ? NumberUtils.INTEGER_ZERO : taskSubmit.getSubStatus());
            taskRecord.setRecordVersion(taskSubmit == null ? NumberUtils.INTEGER_ZERO : taskSubmit.getVersion());
            taskRecord.setExceptionTaskId(taskSubmit == null ? NumberUtils.LONG_ZERO : taskSubmit.getTaskId());
        }

        create(taskRecord);
    }

    /**
     * 初始化超时消息任务履历
     *
     * @param taskRecordNode 履历节点
     * @param taskRecordType 操作类型
     * @param taskSchedule   异常任务定时计划
     * @return
     */
    private ExceptionTaskRecord initExceptionTaskRecord(TaskRecordNode taskRecordNode, TaskRecordType taskRecordType,
                                                        ExceptionTaskSchedule taskSchedule) {
        ExceptionTaskRecord exceptionTaskRecord = new ExceptionTaskRecord();
        exceptionTaskRecord.setRecordNode(taskRecordNode);
        exceptionTaskRecord.setRecordType(taskRecordType);
        exceptionTaskRecord.setRecordStatus(NumberUtils.INTEGER_ZERO);
        exceptionTaskRecord.setRecordSubStatus(NumberUtils.INTEGER_ZERO);
        exceptionTaskRecord.setRecordVersion(taskSchedule.getSourceVersion());
        exceptionTaskRecord.setExceptionTaskId(taskSchedule.getExceptionTaskId());
        exceptionTaskRecord.setRecordId(taskSchedule.getSourceId());
        exceptionTaskRecord.setSubmitUserId(taskSchedule.getSendUserId());
        exceptionTaskRecord.setAcceptUserId(taskSchedule.getAcceptUserId());
        exceptionTaskRecord.setRecordContent(taskSchedule.getSendContent());
        exceptionTaskRecord.setRecordTime(taskSchedule.getPlanSendTime());
        exceptionTaskRecord.setRecordExtendDatas(Collections.emptyList());
        exceptionTaskRecord.setRecordFiles(Collections.emptyList());
        return exceptionTaskRecord;
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsDayTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsDayTrend(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsWeekTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsWeekTrend(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsMonthTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsMonthTrend(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsYearTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsYearTrend(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsDayTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsDayTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsWeekTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsWeekTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsMonthTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsMonthTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }


    /**
     * 初始化协同任务履历
     *
     * @param exceptionTask            异常任务
     * @param exceptionTaskCooperation 协同信息
     */
    private ExceptionTaskRecord initTaskCooperation(@NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskCooperation exceptionTaskCooperation) {
        ExceptionTaskRecord exceptionTaskRecord = initTask(TaskRecordNode.HANDING, exceptionTask, false);
        exceptionTaskRecord.setRecordId(exceptionTaskCooperation.getId());
        exceptionTaskRecord.setRecordStatus(exceptionTaskCooperation.getCooperationStatus().getCode());
        exceptionTaskRecord.setRecordSubStatus(exceptionTaskCooperation.getCooperationSubStatus().getCode());
        exceptionTaskRecord.setRecordVersion(exceptionTaskCooperation.getHandingVersion());

        return exceptionTaskRecord;
    }

    /**
     * 初始化任务履历信息
     *
     * @param recordNode    履历节点
     * @param exceptionTask 异常任务 异常履历必须有的
     * @param isTaskStatus  是否异常任务状态 如果是，异常状态都是异常任务的状态
     * @return 履历
     */
    private ExceptionTaskRecord initTask(@NonNull TaskRecordNode recordNode, @NonNull ExceptionTask exceptionTask, boolean isTaskStatus) {
        ExceptionTaskRecord exceptionTaskRecord = new ExceptionTaskRecord();
        exceptionTaskRecord.setRecordNode(recordNode);
        exceptionTaskRecord.setExceptionTaskCode(exceptionTask.getCode());
        exceptionTaskRecord.setExceptionTaskId(exceptionTask.getId());

        // 异常任务状态时，初始化异常任务的相关信息
        if (isTaskStatus) {
            exceptionTaskRecord.setRecordStatus(exceptionTask.getTaskStatus().getCode());
            exceptionTaskRecord.setRecordSubStatus(exceptionTask.getTaskSubStatus().getCode());
        }

        return exceptionTaskRecord;
    }

}

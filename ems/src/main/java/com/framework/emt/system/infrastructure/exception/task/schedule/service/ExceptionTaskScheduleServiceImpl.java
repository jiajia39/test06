package com.framework.emt.system.infrastructure.exception.task.schedule.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.entity.User;
import com.framework.core.mybatisplus.support.Condition;
import com.framework.core.mybatisplus.support.Query;
import com.framework.core.template.ITemplateService;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.messages.MessageTemplate;
import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.constant.enums.NoticeLevel;
import com.framework.emt.system.domain.messages.request.MessageCancel;
import com.framework.emt.system.domain.messages.request.MessageCancelRequest;
import com.framework.emt.system.domain.messages.request.MessageCreate;
import com.framework.emt.system.domain.messages.request.MessageCreateRequest;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.domain.messages.service.IMessageService;
import com.framework.emt.system.domain.messages.service.IMessageTemplateService;
import com.framework.emt.system.domain.reportnoticeprocess.response.ReportNoticeProcessResponse;
import com.framework.emt.system.domain.reportnoticeprocess.service.ReportNoticeProcessUserService;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.constant.BusinessConstant;
import com.framework.emt.system.infrastructure.constant.NumberConstant;
import com.framework.emt.system.infrastructure.constant.StringConstant;
import com.framework.emt.system.infrastructure.constant.TemplateCodeConstant;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.schedule.ExceptionTaskSchedule;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.code.TaskScheduleErrorCode;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.ExecuteStatus;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.mapper.ExceptionTaskScheduleMapper;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.service.BaseServiceImpl;
import com.framework.emt.system.infrastructure.utils.DataHandleUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常任务定时计划 实现类
 *
 * @author ds_C
 * @since 2023-08-24
 */
@Service
@RequiredArgsConstructor
public class ExceptionTaskScheduleServiceImpl extends BaseServiceImpl<ExceptionTaskScheduleMapper, ExceptionTaskSchedule> implements ExceptionTaskScheduleService {

    private final IMessageService messageService;

    private final IMessageTemplateService messageTemplateService;

    private final ITemplateService templateService;

    private final ReportNoticeProcessUserService reportNoticeProcessUserService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void responseScheduleMessageSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, User responseUser, String itemTitle) {
        // 创建响应超时定时计划、发送响应超时定时消息
        this.responseTimeoutSend(task, taskSubmit, itemTitle);

        // 获取响应超时上报流程推送列表
        List<ReportNoticeProcessResponse> reportProcessList = reportNoticeProcessUserService.findByProcessId(process.getResponseReportNoticeProcessId());
        // 创建响应超时上报流程定时计划、发送响应超时上报流程定时消息
        this.responseTimeoutReportSend(task, taskSubmit, reportProcessList, responseUser, itemTitle);
    }

    @Override
    public void responseScheduleMessageListSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, List<User> responseUsers, List<Long> responseUserIds, String itemTitle) {
        // 创建响应超时定时计划、发送响应超时定时消息
        this.responseTimeoutSends(task, taskSubmit, responseUserIds, itemTitle);

        // 获取响应超时上报流程推送列表
        List<ReportNoticeProcessResponse> reportProcessList = reportNoticeProcessUserService.findByProcessId(process.getResponseReportNoticeProcessId());
        // 创建响应超时上报流程定时计划、发送响应超时上报流程定时消息
        this.responseListTimeoutReportSend(task, taskSubmit, reportProcessList, responseUsers, itemTitle);
    }

    @Override
    public void handingScheduleMessageSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, String itemTitle) {
        // 创建处理超时定时计划、发送处理超时定时消息
        this.handingTimeoutSend(task, taskSubmit, itemTitle);

        // 获取处理超时上报流程推送列表
        List<ReportNoticeProcessResponse> reportProcessList = reportNoticeProcessUserService.findByProcessId(process.getHandingReportNoticeProcessId());
        // 创建处理超时上报流程定时计划、发送处理超时上报流程定时消息
        this.handingTimeoutReportSend(task, taskSubmit, reportProcessList, itemTitle);
    }

    @Override
    public void handingScheduleMessageListSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, List<User> handingUsers, List<Long> handingUserIds, String itemTitle) {
        // 创建响应超时定时计划、发送响应超时定时消息
        this.handingTimeoutSendList(task, taskSubmit, handingUserIds, itemTitle);
        // 获取响应超时上报流程推送列表
        List<ReportNoticeProcessResponse> reportProcessList = reportNoticeProcessUserService.findByProcessId(process.getResponseReportNoticeProcessId());
        // 创建响应超时上报流程定时计划、发送响应超时上报流程定时消息
        this.handingTimeoutReportListSend(task, taskSubmit, reportProcessList, handingUsers, itemTitle);
    }


    /**
     * 1、多个用户创建处理超时定时计划
     * 2、发送处理超时定时消息
     *
     * @param task       异常任务
     * @param taskSubmit 异常提报
     * @param itemTitle  异常项名称
     */
    private void handingTimeoutSendList(ExceptionTask task, ExceptionTaskSubmit taskSubmit, List<Long> handingUserIds, String itemTitle) {
        // 创建处理超时定时计划
        String templateCode = TemplateCodeConstant.HANDING_TIME_OUT;
        TimeOutType timeOutType = TimeOutType.HANDING;
        LocalDateTime deadline = task.getHandingDeadline();
        Integer durationTime = taskSubmit.getHandingDurationTime();
//        Long acceptUserId = NumberUtils.LONG_ZERO;
        List<ExceptionTaskSchedule> schedules = new ArrayList<>();
        handingUserIds.forEach(acceptUserId -> {
            ExceptionTaskSchedule taskSchedule = getExceptionTaskSchedule(task, taskSubmit, templateCode, timeOutType, deadline, durationTime, acceptUserId, itemTitle);
            schedules.add(taskSchedule);
        });

        this.saveBatch(schedules);

        // 发送处理超时定时消息
        Integer typeCode = ExceptionType.HANDING.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(schedules, typeCode);
        messageService.send(messageCreateRequest);
    }

    @Override
    public void cooperationScheduleMessageSend(ExceptionTask task, ExceptionTaskCooperation taskCooperation, User cooperationUser, String itemTitle) {
        // 创建协同超时定时计划、发送协同超时定时消息
        this.cooperationTimeoutSend(task, taskCooperation, itemTitle);

        // 获取协同超时上报流程推送列表
        List<ReportNoticeProcessResponse> reportProcessList = reportNoticeProcessUserService.findByProcessId(taskCooperation.getReportNoticeProcessId());
        // 创建协同超时上报流程定时计划、发送协同超时上报流程定时消息
        this.cooperationTimeoutReportSend(task, taskCooperation, reportProcessList, cooperationUser, itemTitle);
    }

    @Override
    public void resumeScheduleMessageSend(boolean isHandResume, ExceptionTask exceptionTask, ExceptionTaskHanding taskHanding, String itemTitle) {
        if (isHandResume) {
            return;
        }

        String templateCode = TemplateCodeConstant.HANDING_SUSPEND_RESUME;
        MessageTemplate messageTemplate = messageTemplateService.findByCode(templateCode);

        // 创建模版内容
        Map<String, String> hashMap = new HashMap<>(NumberUtils.INTEGER_TWO);
        hashMap.put("code", String.valueOf(exceptionTask.getId()));
        hashMap.put("item", itemTitle);
        String content = templateService.render(hashMap, messageTemplate.getContent());

        Integer exceptionType = ExceptionType.HANDING.getCode();
        Integer noticeLevel = NoticeLevel.NORMAL.getCode();
        MessageCreate messageCreate = new MessageCreate(messageTemplate.getId(), content, exceptionType, taskHanding.getId(), noticeLevel, NumberUtils.LONG_ZERO, taskHanding.getUserId(), exceptionTask.getHandingDeadline());

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        // 发送挂起自动恢复定时消息
        messageService.send(messageCreateRequest);
    }

    @Override
    public void responseScheduleMessageSend(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime) {
        List<TimeOutType> timeOutTypes = TimeOutType.getResponses();
        Integer exceptionType = ExceptionType.RESPONSE.getCode();
        this.scheduleMessageSend(taskSubmit, currentTime, timeOutTypes, exceptionType);
    }

    @Override
    public void handingScheduleMessageSend(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime) {
        List<TimeOutType> timeOutTypes = TimeOutType.getHandings();
        Integer exceptionType = ExceptionType.HANDING.getCode();
        this.scheduleMessageSend(taskSubmit, currentTime, timeOutTypes, exceptionType);
    }

    @Override
    public void responseTimeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime) {
        Integer exceptionType = ExceptionType.RESPONSE.getCode();
        List<TimeOutType> timeOutTypes = TimeOutType.getResponses();
        this.timeOutMessageSendCancel(taskSubmit.getId(), currentTime, exceptionType, timeOutTypes);
    }

    @Override
    public void handingTimeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime) {
        Integer exceptionType = ExceptionType.HANDING.getCode();
        List<TimeOutType> timeOutTypes = TimeOutType.getHandings();
        this.timeOutMessageSendCancel(taskSubmit.getId(), currentTime, exceptionType, timeOutTypes);
    }

    @Override
    public void cooperationTimeOutMessageSendCancel(ExceptionTaskCooperation taskCooperation, LocalDateTime currentTime) {
        Integer exceptionType = ExceptionType.COOPERATION.getCode();
        List<TimeOutType> timeOutTypes = TimeOutType.getCooperations();
        this.timeOutMessageSendCancel(taskCooperation.getId(), currentTime, exceptionType, timeOutTypes);
    }

    @Override
    public void timeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime) {
        // 取消发送响应、响应上报超时和处理、处理上报超时定时消息
        List<MessageCancel> messageCancelList = ExceptionType.getCodes().stream()
                .flatMap(businessType ->
                        NoticeLevel.getCodes().stream().map(level ->
                                new MessageCancel(businessType, level, taskSubmit.getId())
                        ))
                .collect(Collectors.toList());
        MessageCancelRequest messageCancelRequest = new MessageCancelRequest();
        messageCancelRequest.setMessageCancelList(messageCancelList);
        messageService.cancel(messageCancelRequest);

        // 修改异常任务定时计划状态为已取消（只修改当前时间之后未执行的异常任务定时计划）
        List<TimeOutType> timeOutTypes = DataHandleUtils.mergeElements(TimeOutType.getResponses(), TimeOutType.getHandings());
        this.update(new LambdaUpdateWrapper<ExceptionTaskSchedule>()
                .set(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.CANCEL)
                .eq(ExceptionTaskSchedule::getSourceId, taskSubmit.getId())
                .gt(ExceptionTaskSchedule::getPlanSendTime, currentTime)
                .in(ExceptionTaskSchedule::getTimeOutType, timeOutTypes));
    }

    @Override
    public void updateExecuteStatus(MessageToBeSendResponse sendResponse) {
        this.baseMapper.getBySourceIdAndTemplateId(sendResponse.getBusinessId(), sendResponse.getMessageTemplateId(), sendResponse.getSendTime());
    }

    @Override
    public void updateAcceptUser(List<TimeOutType> timeOutTypes, Long sourceId, User acceptUser, Long oldReceiveUserId) {
        // 修改异常任务定时计划接收人
        ExceptionTaskSchedule taskSchedule = this.getOne(new LambdaQueryWrapper<ExceptionTaskSchedule>()
                .eq(ExceptionTaskSchedule::getTimeOutType, timeOutTypes.get(NumberUtils.INTEGER_ZERO))
                .eq(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.NOT)
                .eq(ObjectUtil.isNotNull(oldReceiveUserId), ExceptionTaskSchedule::getAcceptUserId, oldReceiveUserId)
                .eq(ExceptionTaskSchedule::getSourceId, sourceId));
        if (ObjectUtil.isNull(taskSchedule)) {
            return;
        }
        taskSchedule.setAcceptUserId(acceptUser.getId());

        // 修改逐级上报消息内容中的响应人、处理人或协同人
        List<ExceptionTaskSchedule> exceptionTaskSchedules = this.list(new LambdaQueryWrapper<ExceptionTaskSchedule>()
                .eq(ExceptionTaskSchedule::getTimeOutType, timeOutTypes.get(NumberUtils.INTEGER_ONE))
                .eq(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.NOT)
                .eq(ObjectUtil.isNotNull(oldReceiveUserId), ExceptionTaskSchedule::getAcceptUserId, oldReceiveUserId)
                .eq(ExceptionTaskSchedule::getSourceId, sourceId));
        exceptionTaskSchedules.forEach(schedule -> {
            String userNamePosition = BusinessConstant.SEND_USER_NAME_POSITION;
            String replacedContent = schedule.getSendContent().replaceFirst(userNamePosition, "【" + acceptUser.getName() + "】");
            schedule.setSendContent(replacedContent);
        });

        // 批量修改
        exceptionTaskSchedules.add(taskSchedule);
        this.updateBatchById(exceptionTaskSchedules);
    }

    @Override
    public void updateAcceptUser(TimeOutType timeOutType, Long sourceId, Long receiveUserId, Long oldReceiveUserId) {
        ExceptionTaskSchedule taskSchedule = this.getOne(new LambdaQueryWrapper<ExceptionTaskSchedule>()
                .eq(ExceptionTaskSchedule::getTimeOutType, timeOutType)
                .eq(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.NOT)
                .eq(ObjectUtil.isNotNull(oldReceiveUserId), ExceptionTaskSchedule::getAcceptUserId, oldReceiveUserId)
                .eq(ExceptionTaskSchedule::getSourceId, sourceId));
        if (ObjectUtil.isNull(taskSchedule)) {
            return;
        }
        taskSchedule.setAcceptUserId(receiveUserId);

        this.updateById(taskSchedule);
    }

    @Override
    public IPage<TaskScheduleResponse> timeOutReportPage(Integer timeOutType, Long taskId, Query query) {
        return this.baseMapper.timeOutReportPage(Condition.getPage(query), timeOutType, taskId);
    }

    /**
     * 根据id查询异常任务定时计划
     * 若未查询到数据则抛出异常
     *
     * @param id 异常任务定时计划id
     * @return
     */
    public ExceptionTaskSchedule findByIdThrowErr(Long id) {
        return this.findByIdThrowErr(id, TaskScheduleErrorCode.NOT_FOUND);
    }

    /**
     * 1、创建响应超时定时计划
     * 2、发送响应超时定时消息
     *
     * @param task       异常任务
     * @param taskSubmit 异常提报
     * @param itemTitle  异常项名称
     */
    private void responseTimeoutSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, String itemTitle) {
        // 创建响应超时定时计划
        String templateCode = TemplateCodeConstant.RESPONSE_TIME_OUT;
        TimeOutType timeOutType = TimeOutType.RESPONSE;
        LocalDateTime deadline = task.getResponseDeadline();
        Integer durationTime = taskSubmit.getResponseDurationTime();
        Long acceptUserId = taskSubmit.getSubmitResponseUserId();
        ExceptionTaskSchedule taskSchedule = getExceptionTaskSchedule(
                task, taskSubmit, templateCode, timeOutType, deadline, durationTime, acceptUserId, itemTitle
        );
        this.create(taskSchedule);

        // 发送响应超时定时消息
        Integer typeCode = ExceptionType.RESPONSE.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskSchedule, typeCode);
        messageService.send(messageCreateRequest);
    }


    /**
     * 1、创建响应超时上报流程定时计划
     * 2、发送响应超时上报流程定时消息
     *
     * @param task              异常任务
     * @param taskSubmit        异常提报
     * @param reportProcessList 逐级上报流程列表
     * @param responseUser      响应人
     * @param itemTitle         异常项名称
     */
    private void responseTimeoutReportSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                           List<ReportNoticeProcessResponse> reportProcessList,
                                           User responseUser, String itemTitle) {
        // 创建响应超时上报定时计划
        if (CollectionUtil.isEmpty(reportProcessList)) {
            return;
        }
        String responseUserName = responseUser.getName();
        String templateCode = TemplateCodeConstant.RESPONSE_TIME_OUT_REPORT;
        TimeOutType timeOutType = TimeOutType.RESPONSE_REPORT;
        LocalDateTime deadline = task.getResponseDeadline();
        List<ExceptionTaskSchedule> taskScheduleList = getTaskScheduleList(
                task, taskSubmit, reportProcessList, responseUserName, templateCode, timeOutType, deadline, itemTitle
        );
        this.saveBatch(taskScheduleList);

        // 发送响应超时上报流程定时消息
        Integer typeCode = ExceptionType.RESPONSE.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskScheduleList, typeCode);
        messageService.send(messageCreateRequest);
    }

    /**
     * 1、创建处理超时定时计划
     * 2、发送处理超时定时消息
     *
     * @param task       异常任务
     * @param taskSubmit 异常提报
     * @param itemTitle  异常项名称
     */
    private void handingTimeoutSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, String itemTitle) {
        // 创建处理超时定时计划
        String templateCode = TemplateCodeConstant.HANDING_TIME_OUT;
        TimeOutType timeOutType = TimeOutType.HANDING;
        LocalDateTime deadline = task.getHandingDeadline();
        Integer durationTime = taskSubmit.getHandingDurationTime();
        Long acceptUserId = NumberUtils.LONG_ZERO;
        ExceptionTaskSchedule taskSchedule = getExceptionTaskSchedule(
                task, taskSubmit, templateCode, timeOutType, deadline, durationTime, acceptUserId, itemTitle
        );
        this.create(taskSchedule);

        // 发送处理超时定时消息
        Integer typeCode = ExceptionType.HANDING.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskSchedule, typeCode);
        messageService.send(messageCreateRequest);
    }

    /**
     * 1、创建处理超时上报流程定时计划
     * 2、发送处理超时上报流程定时消息
     *
     * @param task              异常任务
     * @param taskSubmit        异常提报
     * @param reportProcessList 逐级上报流程列表
     */
    private void handingTimeoutReportSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                          List<ReportNoticeProcessResponse> reportProcessList, String itemTitle) {
        // 创建处理超时上报流程定时计划
        if (CollectionUtil.isEmpty(reportProcessList)) {
            return;
        }
        String templateCode = TemplateCodeConstant.HANDING_TIME_OUT_REPORT;
        TimeOutType timeOutType = TimeOutType.HANDING_REPORT;
        LocalDateTime deadline = task.getHandingDeadline();
        List<ExceptionTaskSchedule> taskScheduleList = getTaskScheduleList(
                task, taskSubmit, reportProcessList, null, templateCode, timeOutType, deadline, itemTitle
        );
        this.saveBatch(taskScheduleList);

        // 发送处理超时上报流程定时消息
        Integer exceptionTypeCode = ExceptionType.HANDING.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskScheduleList, exceptionTypeCode);
        messageService.send(messageCreateRequest);
    }


    /**
     * 根据当前时间创建响应和处理超时、响应和处理超时上报定时消息并发送
     *
     * @param taskSubmit    异常提拔
     * @param currentTime   当前时间
     * @param timeOutTypes  超时类型列表
     * @param exceptionType 异常类型
     */
    private void scheduleMessageSend(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime,
                                     List<TimeOutType> timeOutTypes, Integer exceptionType) {
        // 获取当前时间之后的响应和处理超时、超时上报异常任务定时计划
        List<ExceptionTaskSchedule> taskScheduleList = this.list(new LambdaQueryWrapper<ExceptionTaskSchedule>()
                .eq(ExceptionTaskSchedule::getSourceId, taskSubmit.getId())
                .gt(ExceptionTaskSchedule::getPlanSendTime, currentTime)
                .in(ExceptionTaskSchedule::getTimeOutType, timeOutTypes));

        if (CollectionUtil.isEmpty(taskScheduleList)) {
            return;
        }

        // 重新发送响应和处理超时、超时上报消息
        List<MessageCreate> messageCreateList = new ArrayList<>();

        for (ExceptionTaskSchedule taskSchedule : taskScheduleList) {
            MessageCreate messageCreate = new MessageCreate();
            messageCreate.setMessageTemplateId(taskSchedule.getTemplateId());
            messageCreate.setContent(taskSchedule.getSendContent());
            messageCreate.setBusinessId(taskSchedule.getSourceId());
            messageCreate.setSendUserId(taskSchedule.getSendUserId());
            messageCreate.setReceiveUserId(taskSchedule.getAcceptUserId());
            messageCreate.setBusinessType(exceptionType);
            messageCreate.setSendTime(taskSchedule.getPlanSendTime());

            if (timeOutTypes.get(NumberUtils.INTEGER_ZERO).equals(taskSchedule.getTimeOutType())) {
                messageCreate.setLevel(NoticeLevel.OVERTIME_WARNING.getCode());
            }
            if (timeOutTypes.get(NumberUtils.INTEGER_ONE).equals(taskSchedule.getTimeOutType())) {
                messageCreate.setLevel(NoticeLevel.OVERTIME_REPORT.getCode());
            }

            messageCreateList.add(messageCreate);
        }

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(messageCreateList);

        messageService.send(messageCreateRequest);

        // 修改异常任务定时计划状态为未执行
        taskScheduleList.forEach(taskSchedule -> taskSchedule.setExecuteStatus(ExecuteStatus.NOT));
        this.updateBatchById(taskScheduleList);
    }

    /**
     * 1、创建协同超时定时计划
     * 2、发送协同超时定时消息
     *
     * @param task            异常任务
     * @param taskCooperation 异常协同
     * @param itemTitle       异常项名称
     */
    private void cooperationTimeoutSend(ExceptionTask task, ExceptionTaskCooperation taskCooperation, String itemTitle) {
        // 创建协同超时定时计划
        String templateCode = TemplateCodeConstant.COOPERATION_TIME_OUT;
        TimeOutType timeOutType = TimeOutType.COOPERATION;
        LocalDateTime deadline = taskCooperation.getFinishDeadline();
        Integer durationTime = taskCooperation.getDurationTime();
        Long acceptUserId = taskCooperation.getUserId();
        ExceptionTaskSchedule taskSchedule = getExceptionTaskSchedule(
                task, taskCooperation, templateCode, timeOutType, deadline, durationTime, acceptUserId, itemTitle
        );
        this.create(taskSchedule);

        // 发送协同超时定时消息
        Integer typeCode = ExceptionType.COOPERATION.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskSchedule, typeCode);
        messageService.send(messageCreateRequest);
    }

    /**
     * 1、创建协同超时上报流程定时计划
     * 2、发送协同超时上报流程定时消息
     *
     * @param task              异常任务
     * @param cooperation       异常协同
     * @param reportProcessList 逐级上报流程列表
     * @param itemTitle         异常项名称
     */
    private void cooperationTimeoutReportSend(ExceptionTask task, ExceptionTaskCooperation cooperation,
                                              List<ReportNoticeProcessResponse> reportProcessList,
                                              User cooperationUser, String itemTitle) {
        // 创建协同超时上报流程定时计划
        if (CollectionUtil.isEmpty(reportProcessList)) {
            return;
        }
        String cooperationUserName = cooperationUser.getName();
        String templateCode = TemplateCodeConstant.COOPERATION_TIME_OUT_REPORT;
        TimeOutType timeOutType = TimeOutType.COOPERATION_REPORT;
        LocalDateTime deadline = cooperation.getFinishDeadline();
        List<ExceptionTaskSchedule> taskScheduleList = getTaskSchedulesList(
                task, cooperation, reportProcessList, cooperationUserName, templateCode, timeOutType, deadline, itemTitle
        );
        this.saveBatch(taskScheduleList);

        // 发送协同超时上报流程定时消息
        Integer exceptionTypeCode = ExceptionType.COOPERATION.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskScheduleList, exceptionTypeCode);
        messageService.send(messageCreateRequest);
    }

    /**
     * 1、取消发送超时、超时上报定时消息
     * 2、修改异常任务定时计划状态为已取消
     *
     * @param sourceId      超时表id
     * @param exceptionType 当前时间
     * @param exceptionType 异常类型
     * @param timeOutTypes  超时类型列表
     */
    private void timeOutMessageSendCancel(Long sourceId, LocalDateTime currentTime,
                                          Integer exceptionType, List<TimeOutType> timeOutTypes) {
        // 取消发送超时、超时上报定时消息
        List<MessageCancel> messageCancelList = NoticeLevel.getCodes().stream()
                .map(level ->
                        new MessageCancel(exceptionType, level, sourceId)
                ).collect(Collectors.toList());

        MessageCancelRequest messageCancelRequest = new MessageCancelRequest();
        messageCancelRequest.setMessageCancelList(messageCancelList);

        messageService.cancel(messageCancelRequest);

        // 修改异常任务定时计划状态为已取消（只修改当前时间之后未执行的异常任务定时计划）
        this.update(new LambdaUpdateWrapper<ExceptionTaskSchedule>()
                .set(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.CANCEL)
                .eq(ExceptionTaskSchedule::getSourceId, sourceId)
                .gt(ExceptionTaskSchedule::getPlanSendTime, currentTime)
                .in(ExceptionTaskSchedule::getTimeOutType, timeOutTypes));
    }

    /**
     * 初始化超时消息创建参数
     *
     * @param taskSchedule 异常任务定时计划
     * @param typeCode     异常类型code
     */
    private MessageCreateRequest initMessageCreateRequest(ExceptionTaskSchedule taskSchedule, Integer typeCode) {
        MessageCreate messageCreate = new MessageCreate(taskSchedule.getTemplateId(), taskSchedule.getSendContent(), typeCode, taskSchedule.getSourceId(), NoticeLevel.OVERTIME_WARNING.getCode(), taskSchedule.getSendUserId(), taskSchedule.getAcceptUserId(), taskSchedule.getPlanSendTime());

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(Lists.newArrayList(messageCreate));

        return messageCreateRequest;
    }

    /**
     * 初始化超时上报消息创建参数
     *
     * @param taskScheduleList 异常任务定时计划列表
     * @param typeCode         异常类型code
     */
    private MessageCreateRequest initMessageCreateRequest(List<ExceptionTaskSchedule> taskScheduleList, Integer typeCode) {
        List<MessageCreate> messageCreateList = taskScheduleList.stream()
                .map(taskSchedule -> new MessageCreate(taskSchedule.getTemplateId(), taskSchedule.getSendContent(), typeCode, taskSchedule.getSourceId(), NoticeLevel.OVERTIME_REPORT.getCode(), taskSchedule.getSendUserId(), taskSchedule.getAcceptUserId(), taskSchedule.getPlanSendTime())).collect(Collectors.toList());

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        messageCreateRequest.setMessageCreateList(messageCreateList);

        return messageCreateRequest;
    }

    /**
     * 获取处理或响应超时定时计划创建实体
     *
     * @param task         异常任务
     * @param taskSubmit   异常提报
     * @param templateCode 模板code
     * @param timeOutType  超时类型
     * @param deadline     最后期限
     * @param durationTime 超时时限
     * @param acceptUserId 接收人id
     * @param itemTitle    异常项名称
     * @return
     */
    private ExceptionTaskSchedule getExceptionTaskSchedule(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                                           String templateCode, TimeOutType timeOutType, LocalDateTime deadline,
                                                           Integer durationTime, Long acceptUserId, String itemTitle) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(templateCode);

        ExceptionTaskSchedule taskSchedule = new ExceptionTaskSchedule();

        // 创建模版内容
        Map<String, String> hashMap = new HashMap<>(NumberUtils.INTEGER_TWO);
        hashMap.put("code", String.valueOf(task.getId()));
        hashMap.put("item", itemTitle);
        String content = templateService.render(hashMap, messageTemplate.getContent());

        Long taskId = task.getId();
        Long submitId = taskSubmit.getId();
        Integer submitVersion = taskSubmit.getSubmitVersion();
        Long templateId = messageTemplate.getId();
        Long submitUserId = taskSubmit.getSubmitUserId();

        taskSchedule.initTimeout(taskId, timeOutType, submitId, submitVersion, deadline,
                templateId, content, submitUserId, acceptUserId, durationTime);

        return taskSchedule;
    }

    /**
     * 获取协同超时定时计划创建实体
     *
     * @param exceptionTask   异常任务
     * @param taskCooperation 异常协同
     * @param templateCode    模板code
     * @param timeOutType     超时类型
     * @param deadline        最后期限
     * @param durationTime    超时时限
     * @param acceptUserId    接收人id
     * @param itemTitle       异常项名称
     * @return
     */
    private ExceptionTaskSchedule getExceptionTaskSchedule(ExceptionTask exceptionTask, ExceptionTaskCooperation taskCooperation,
                                                           String templateCode, TimeOutType timeOutType, LocalDateTime deadline,
                                                           Integer durationTime, Long acceptUserId, String itemTitle) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(templateCode);

        ExceptionTaskSchedule taskSchedule = new ExceptionTaskSchedule();

        // 创建模版内容
        Map<String, String> hashMap = new HashMap<>(NumberConstant.THREE);
        hashMap.put("code", String.valueOf(exceptionTask.getId()));
        hashMap.put("item", itemTitle);
        hashMap.put("cooperationTask", taskCooperation.getTitle());
        String content = templateService.render(hashMap, messageTemplate.getContent());

        Long taskId = exceptionTask.getId();
        Long cooperationId = taskCooperation.getId();
        Integer handingVersion = taskCooperation.getHandingVersion();
        Long planUserId = taskCooperation.getPlanUserId();
        Long templateId = messageTemplate.getId();

        taskSchedule.initTimeout(taskId, timeOutType, cooperationId, handingVersion, deadline,
                templateId, content, planUserId, acceptUserId, durationTime);

        return taskSchedule;
    }

    /**
     * 获取处理或响应超时上报定时计划创建列表
     *
     * @param task              异常任务
     * @param taskSubmit        异常提报
     * @param reportProcessList 上报消息和上报人列表
     * @param userName          响应人姓名/处理人姓名
     * @param templateCode      模板code
     * @param timeOutType       超时类型
     * @param deadline          最后期限
     * @param itemTitle         异常项名称
     * @return
     */
    private List<ExceptionTaskSchedule> getTaskScheduleList(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                                            List<ReportNoticeProcessResponse> reportProcessList,
                                                            String userName, String templateCode, TimeOutType timeOutType,
                                                            LocalDateTime deadline, String itemTitle) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(templateCode);

        List<ExceptionTaskSchedule> taskSchedules = new ArrayList<>();
        for (ReportNoticeProcessResponse reportProcess : reportProcessList) {
            Integer timeLimit = reportProcess.getTimeLimit();
            List<String> receiveUserIds = DataHandleUtils.splitStr(reportProcess.getReceiveUserIds());

            for (String receiveUserId : receiveUserIds) {
                ExceptionTaskSchedule taskSchedule = new ExceptionTaskSchedule();

                // 创建模版内容
                Map<String, String> hashMap = new HashMap<>(NumberConstant.FIVE);
                hashMap.put("code", String.valueOf(task.getId()));
                hashMap.put("item", itemTitle);
                hashMap.put("timeUnit", StringConstant.MINUTES);
                if (userName != null) {
                    hashMap.put("operator", userName);
                }
                hashMap.put("time", String.valueOf(timeLimit));

                String sendContent = templateService.render(hashMap, messageTemplate.getContent());

                Long taskId = task.getId();
                Long submitId = taskSubmit.getId();
                Integer submitVersion = taskSubmit.getSubmitVersion();
                LocalDateTime sendTime = deadline.plusMinutes(timeLimit);
                Long templateId = messageTemplate.getId();
                Long submitUserId = taskSubmit.getSubmitUserId();
                Long processId = reportProcess.getReportNoticeProcessId();
                String processName = reportProcess.getName();

                taskSchedule.initTimeoutReport(taskId, timeOutType, submitId, submitVersion, timeLimit, sendTime,
                        templateId, sendContent, submitUserId, receiveUserId, processId, processName);

                taskSchedules.add(taskSchedule);
            }
        }

        return taskSchedules;
    }

    /**
     * 获取协同超时上报定时计划创建列表
     *
     * @param task                异常任务
     * @param cooperation         异常协同
     * @param reportProcessList   上报消息和上报人列表
     * @param cooperationUserName 协同人姓名
     * @param templateCode        模板code
     * @param timeOutType         超时类型
     * @param deadline            最后期限
     * @param itemTitle           异常项名称
     * @return
     */
    private List<ExceptionTaskSchedule> getTaskSchedulesList(ExceptionTask task, ExceptionTaskCooperation cooperation,
                                                             List<ReportNoticeProcessResponse> reportProcessList,
                                                             String cooperationUserName, String templateCode,
                                                             TimeOutType timeOutType, LocalDateTime deadline, String itemTitle) {
        MessageTemplate messageTemplate = messageTemplateService.findByCode(templateCode);
        List<ExceptionTaskSchedule> taskSchedules = new ArrayList<>();

        for (ReportNoticeProcessResponse reportProcess : reportProcessList) {
            Integer timeLimit = reportProcess.getTimeLimit();
            List<String> receiveUserIds = DataHandleUtils.splitStr(reportProcess.getReceiveUserIds());

            for (String receiveUserId : receiveUserIds) {
                ExceptionTaskSchedule taskSchedule = new ExceptionTaskSchedule();

                // 创建模版内容
                Map<String, String> hashMap = new HashMap<>(NumberConstant.SIX);
                hashMap.put("code", String.valueOf(task.getId()));
                hashMap.put("item", itemTitle);
                hashMap.put("timeUnit", StringConstant.MINUTES);
                hashMap.put("operator", cooperationUserName);
                hashMap.put("time", String.valueOf(timeLimit));
                hashMap.put("cooperationTask", cooperation.getTitle());

                String sendContent = templateService.render(hashMap, messageTemplate.getContent());

                Long taskId = task.getId();
                Long cooperationId = cooperation.getId();
                Integer handingVersion = cooperation.getHandingVersion();
                LocalDateTime sendTime = deadline.plusMinutes(timeLimit);
                Long templateId = messageTemplate.getId();
                Long userId = cooperation.getUserId();
                Long processId = cooperation.getReportNoticeProcessId();
                String processTitle = cooperation.getReportNoticeProcessName();

                taskSchedule.initTimeoutReport(taskId, timeOutType, cooperationId, handingVersion, timeLimit,
                        sendTime, templateId, sendContent, userId, receiveUserId, processId, processTitle);

                taskSchedules.add(taskSchedule);
            }
        }

        return taskSchedules;
    }


    /**
     * 1、创建多人响应超时定时计划
     * 2、发送响应超时定时消息
     *
     * @param task       异常任务
     * @param taskSubmit 异常提报
     * @param itemTitle  异常项名称
     */
    private void responseTimeoutSends(ExceptionTask task, ExceptionTaskSubmit taskSubmit, List<Long> responseUserIds, String itemTitle) {
        // 创建响应超时定时计划
        String templateCode = TemplateCodeConstant.RESPONSE_TIME_OUT;
        TimeOutType timeOutType = TimeOutType.RESPONSE;
        LocalDateTime deadline = task.getResponseDeadline();
        Integer durationTime = taskSubmit.getResponseDurationTime();
        List<ExceptionTaskSchedule> schedules = new ArrayList<>();
        // 发送响应超时定时消息
        Integer typeCode = ExceptionType.RESPONSE.getCode();
        responseUserIds.forEach(responseUserId -> {
            ExceptionTaskSchedule taskSchedule = getExceptionTaskSchedule(
                    task, taskSubmit, templateCode, timeOutType, deadline, durationTime, responseUserId, itemTitle
            );
            schedules.add(taskSchedule);
        });

        this.saveBatch(schedules);
        MessageCreateRequest messageCreateRequest = initMessageCreateRequests(schedules, typeCode);
        messageService.send(messageCreateRequest);
    }

    /**
     * 初始化超时消息创建参数
     *
     * @param taskSchedules 异常任务定时计划
     * @param typeCode      异常类型code
     */
    private MessageCreateRequest initMessageCreateRequests(List<ExceptionTaskSchedule> taskSchedules, Integer typeCode) {

        MessageCreateRequest messageCreateRequest = new MessageCreateRequest();
        List<MessageCreate> messageCreateRequests = new ArrayList<>();
        taskSchedules.forEach(taskSchedule -> {
            MessageCreate messageCreate = new MessageCreate(taskSchedule.getTemplateId(), taskSchedule.getSendContent(), typeCode, taskSchedule.getSourceId(), NoticeLevel.OVERTIME_WARNING.getCode(), taskSchedule.getSendUserId(), taskSchedule.getAcceptUserId(), taskSchedule.getPlanSendTime());
            messageCreateRequests.add(messageCreate);
        });
        messageCreateRequest.setMessageCreateList(messageCreateRequests);
        return messageCreateRequest;
    }

    /**
     * 1、多个响应人创建响应超时上报流程定时计划
     * 2、发送响应超时上报流程定时消息
     *
     * @param task              异常任务
     * @param taskSubmit        异常提报
     * @param reportProcessList 逐级上报流程列表
     * @param responseUsers     响应人列表
     * @param itemTitle         异常项名称
     */
    private void responseListTimeoutReportSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                               List<ReportNoticeProcessResponse> reportProcessList,
                                               List<User> responseUsers, String itemTitle) {
        // 创建响应超时上报定时计划
        if (CollectionUtil.isEmpty(reportProcessList)) {
            return;
        }
        String responseUserNames = responseUsers.stream().map(User::getName).collect(Collectors.joining(StrPool.COMMA));
        String templateCode = TemplateCodeConstant.RESPONSE_TIME_OUT_REPORT;
        TimeOutType timeOutType = TimeOutType.RESPONSE_REPORT;
        LocalDateTime deadline = task.getResponseDeadline();
        List<ExceptionTaskSchedule> taskScheduleList = getTaskScheduleList(
                task, taskSubmit, reportProcessList, responseUserNames, templateCode, timeOutType, deadline, itemTitle
        );
        this.saveBatch(taskScheduleList);

        // 发送响应超时上报流程定时消息
        Integer typeCode = ExceptionType.RESPONSE.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskScheduleList, typeCode);
        messageService.send(messageCreateRequest);
    }


    /**
     * 1、多个处理人创建处理超时上报流程定时计划
     * 2、发送处理超时上报流程定时消息
     *
     * @param task              异常任务
     * @param taskSubmit        异常提报
     * @param reportProcessList 逐级上报流程列表
     */
    private void handingTimeoutReportListSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit,
                                              List<ReportNoticeProcessResponse> reportProcessList, List<User> handingUsers, String itemTitle) {
        // 创建处理超时上报流程定时计划
        if (CollectionUtil.isEmpty(reportProcessList)) {
            return;
        }
        String templateCode = TemplateCodeConstant.HANDING_TIME_OUT_REPORT;
        TimeOutType timeOutType = TimeOutType.HANDING_REPORT;
        LocalDateTime deadline = task.getHandingDeadline();
        String handingUserNames = handingUsers.stream().map(User::getName).collect(Collectors.joining(StrPool.COMMA));
        List<ExceptionTaskSchedule> taskScheduleList = getTaskScheduleList(
                task, taskSubmit, reportProcessList, handingUserNames, templateCode, timeOutType, deadline, itemTitle
        );
        this.saveBatch(taskScheduleList);

        // 发送处理超时上报流程定时消息
        Integer exceptionTypeCode = ExceptionType.HANDING.getCode();
        MessageCreateRequest messageCreateRequest = initMessageCreateRequest(taskScheduleList, exceptionTypeCode);
        messageService.send(messageCreateRequest);
    }

    @Override
    public void responseTimeOutMessageCancelReceive(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime, List<Long> userIds) {
        Integer exceptionType = ExceptionType.RESPONSE.getCode();
        List<TimeOutType> timeOutTypes = TimeOutType.getResponses();
        this.timeOutMessageSendCancelByUserId(taskSubmit.getId(), currentTime, exceptionType, timeOutTypes, userIds);
    }


    @Override
    public void handingTimeOutMessageSendCancelByReceive(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime, List<Long> userIds) {
        Integer exceptionType = ExceptionType.HANDING.getCode();
        List<TimeOutType> timeOutTypes = TimeOutType.getHandings();
        this.timeOutMessageSendCancelByUserId(taskSubmit.getId(), currentTime, exceptionType, timeOutTypes, userIds);
    }

    /**
     * 1、取消发送超时、超时上报定时消息
     * 2、修改异常任务定时计划状态为已取消
     *
     * @param sourceId      超时表id
     * @param exceptionType 当前时间
     * @param exceptionType 异常类型
     * @param timeOutTypes  超时类型列表
     * @param userIds       需要取消的用户id列表
     */
    private void timeOutMessageSendCancelByUserId(Long sourceId, LocalDateTime currentTime,
                                                  Integer exceptionType, List<TimeOutType> timeOutTypes, List<Long> userIds) {
        // 取消发送超时、超时上报定时消息
        List<MessageCancel> messageCancelList = new ArrayList<>();
        NoticeLevel.getCodes().forEach(level -> {
            userIds.forEach(userId -> {
                MessageCancel messageCancel = new MessageCancel();
                messageCancelList.add(messageCancel.cancel(exceptionType, level, sourceId, userId));
            });
        });

        MessageCancelRequest messageCancelRequest = new MessageCancelRequest();
        messageCancelRequest.setMessageCancelList(messageCancelList);

        messageService.cancel(messageCancelRequest);

        // 修改异常任务定时计划状态为已取消（只修改当前时间之后未执行的异常任务定时计划）
        this.update(new LambdaUpdateWrapper<ExceptionTaskSchedule>()
                .set(ExceptionTaskSchedule::getExecuteStatus, ExecuteStatus.CANCEL)
                .eq(ExceptionTaskSchedule::getSourceId, sourceId)
                .in(ExceptionTaskSchedule::getAcceptUserId, userIds)
                .gt(ExceptionTaskSchedule::getPlanSendTime, currentTime)
                .in(ExceptionTaskSchedule::getTimeOutType, timeOutTypes));
    }


    @Override
    public List<StatisticsTrendValueResponse> statisticsDayTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsDayTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsMonthTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsMonthTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);
    }

    @Override
    public List<StatisticsTrendValueResponse> statisticsYearTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId) {
        return this.baseMapper.statisticsYearTimeout(type, startDate, endDate, deptIds, exceptionCategoryId, exceptionProcessId, workspaceLocationId);

    }

}

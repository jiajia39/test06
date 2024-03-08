package com.framework.emt.system.infrastructure.exception.task.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.admin.system.entity.User;
import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.exception.ExceptionProcess;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.schedule.ExceptionTaskSchedule;
import com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums.TimeOutType;
import com.framework.emt.system.infrastructure.exception.task.schedule.response.TaskScheduleResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.service.BaseService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务定时计划 服务层
 *
 * @author ds_C
 * @since 2023-08-24
 */
public interface ExceptionTaskScheduleService extends BaseService<ExceptionTaskSchedule> {

    /**
     * 发送响应超时、响应超时上报定时消息
     *
     * @param task         异常任务
     * @param taskSubmit   异常提报
     * @param process      异常流程
     * @param responseUser 响应人
     * @param itemTitle    异常项名称
     */
    void responseScheduleMessageSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, User responseUser, String itemTitle);

    /**
     * 发送多人响应超时、响应超时上报定时消息
     *
     * @param task            异常任务
     * @param taskSubmit      异常提报
     * @param process         异常流程
     * @param responseUsers   响应人列表
     * @param responseUserIds 响应人id列表
     * @param itemTitle       异常项名称
     */
    void responseScheduleMessageListSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, List<User> responseUsers, List<Long> responseUserIds, String itemTitle);

    /**
     * 发送处理超时、处理超时上报定时消息
     *
     * @param task       异常任务
     * @param taskSubmit 异常提报
     * @param process    异常流程
     * @param itemTitle  异常项名称
     */
    void handingScheduleMessageSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, String itemTitle);

    /**
     * 发送多人处理超时、处理超时上报定时消息
     *
     * @param task            异常任务
     * @param taskSubmit      异常提报
     * @param process         异常流程
     * @param responseUsers   处理人列表
     * @param responseUserIds 处理人id列表
     * @param itemTitle       异常项名称
     */
    void handingScheduleMessageListSend(ExceptionTask task, ExceptionTaskSubmit taskSubmit, ExceptionProcess process, List<User> responseUsers, List<Long> responseUserIds, String itemTitle);

    /**
     * 发送协同超时、协同超时上报定时消息
     *
     * @param task            异常任务
     * @param taskCooperation 异常流程
     * @param cooperationUser 协同人
     * @param itemTitle       异常项名称
     */
    void cooperationScheduleMessageSend(ExceptionTask task, ExceptionTaskCooperation taskCooperation, User cooperationUser, String itemTitle);

    /**
     * 发送挂起自动恢复定时消息
     *
     * @param isHandResume  是否手动挂起
     * @param exceptionTask 异常任务
     * @param taskHanding   异常处理
     * @param itemTitle     异常项名称
     */
    void resumeScheduleMessageSend(boolean isHandResume, ExceptionTask exceptionTask, ExceptionTaskHanding taskHanding, String itemTitle);

    /**
     * 根据当前时间创建响应超时、响应超时上报定时消息并发送
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     */
    void responseScheduleMessageSend(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime);

    /**
     * 根据当前时间创建处理超时、处理超时上报定时消息并发送
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     */
    void handingScheduleMessageSend(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime);

    /**
     * 取消发送响应、响应上报超时定时消息
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     */
    void responseTimeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime);


    /**
     * 取消除了接收用户外其他用户的发送响应、响应上报超时定时消息
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     * @param userIds     用户id列表
     */
    void responseTimeOutMessageCancelReceive(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime, List<Long> userIds);

    /**
     * 取消发送处理、处理上报超时定时消息
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     */
    void handingTimeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime);

    /**
     * 取消发送处理、处理上报超时定时消息
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     * @param userIds     用户id列表
     */
    void handingTimeOutMessageSendCancelByReceive(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime, List<Long> userIds);

    /**
     * 取消发送协同、协同上报超时定时消息
     *
     * @param taskCooperation 异常协同
     * @param currentTime     当前时间
     */
    void cooperationTimeOutMessageSendCancel(ExceptionTaskCooperation taskCooperation, LocalDateTime currentTime);

    /**
     * 取消发送响应、响应上报超时和处理、处理上报超时定时消息
     *
     * @param taskSubmit  异常提报
     * @param currentTime 当前时间
     */
    void timeOutMessageSendCancel(ExceptionTaskSubmit taskSubmit, LocalDateTime currentTime);

    /**
     * 修改异常任务定时计划的执行状态为已执行
     *
     * @param sendResponse 消息待发送数据
     * @return
     */
    void updateExecuteStatus(MessageToBeSendResponse sendResponse);

    /**
     * 1、修改异常任务定时计划接收人
     * 2、修改逐级上报发送内容中的响应人、处理人或协同人
     *
     * @param timeOutTypes 超时类型列表
     * @param sourceId     超时表id
     * @param acceptUser   接收人
     */
    void updateAcceptUser(List<TimeOutType> timeOutTypes, Long sourceId, User acceptUser, Long oldReceiveUserId);

    /**
     * 修改异常任务定时计划接收人
     *
     * @param timeOutType  超时类型
     * @param sourceId     超时表id
     * @param acceptUserId 接收人id
     */
    void updateAcceptUser(TimeOutType timeOutType, Long sourceId, Long acceptUserId, Long oldReceiveUserId);

    /**
     * 超时上报流程分页列表
     *
     * @param timeOutType 超时类型code
     * @param taskId      任务表id
     * @param query       分页查询条件
     * @return
     */
    IPage<TaskScheduleResponse> timeOutReportPage(Integer timeOutType, Long taskId, Query query);

    /**
     * 每天响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsDayTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 每月响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */

    List<StatisticsTrendValueResponse> statisticsMonthTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);


    /**
     * 每年响应、处理、协同超时次数
     *
     * @param type                类型
     * @param startDate           开始时间
     * @param endDate             结束时间
     * @param deptIds             部门id
     * @param exceptionCategoryId 异常分类id
     * @param exceptionProcessId  异常流程id
     * @param workspaceLocationId 作业单元id
     * @return 数量和日期
     */

    List<StatisticsTrendValueResponse> statisticsYearTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

}

package com.framework.emt.system.infrastructure.exception.task.record.service;


import com.framework.admin.system.entity.User;
import com.framework.common.auth.entity.FtUser;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.messages.response.MessageToBeSendResponse;
import com.framework.emt.system.domain.statistics.response.StatisticsTrendValueResponse;
import com.framework.emt.system.infrastructure.exception.task.check.ExceptionTaskCheck;
import com.framework.emt.system.infrastructure.exception.task.cooperation.ExceptionTaskCooperation;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import com.framework.emt.system.infrastructure.exception.task.record.ExceptionTaskRecord;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import com.framework.emt.system.infrastructure.exception.task.response.ExceptionTaskResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.ExceptionTaskSubmit;
import com.framework.emt.system.infrastructure.exception.task.task.ExceptionTask;
import com.framework.emt.system.infrastructure.service.BaseService;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 异常任务履历 服务层
 *
 * @author ds_C
 * @since 2023-08-08
 */
public interface ExceptionTaskRecordService extends BaseService<ExceptionTaskRecord> {

    /**
     * 异常提报节点创建记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     */
    void submitNodeCreate(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit);

    /**
     * 异常提报节点编辑记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     */
    void submitNodeUpdate(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit);

    /**
     * 异常提报节点编辑记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     * @param responseUser        响应人
     */
    void submitNodeSubmit(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit, @NonNull List<User> responseUser);

    /**
     * 异常提报节点关闭记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     */
    void submitNodeClose(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit);

    /**
     * 异常提报撤销记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     */
    void submitNodeCancel(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit);


    /**
     * 异常提报节点删除记录
     *
     * @param user                操作人
     * @param exceptionTask       异常任务
     * @param exceptionTaskSubmit 异常提报
     */
    void submitNodeDelete(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull ExceptionTaskSubmit exceptionTaskSubmit);

    /**
     * 异常响应节点驳回记录
     *
     * @param user                驳回人
     * @param exceptionTask       异常任务
     * @param taskResponseId      响应id
     * @param exceptionTaskSubmit 提交记录
     * @param currentTime         驳回时间
     * @param rejectReason        驳回原因
     */
    void responseNodeReject(@NonNull FtUser user, @NonNull ExceptionTask exceptionTask, @NonNull Long taskResponseId,
                            @NonNull ExceptionTaskSubmit exceptionTaskSubmit, LocalDateTime currentTime, String rejectReason);

    /**
     * 响应转派
     *
     * @param user           转派人
     * @param transferUser   被转派人
     * @param exceptionTask  异常任务
     * @param taskResponseId 响应id
     * @param otherTime      转派时间
     */
    void responseTransfer(@NonNull FtUser user, @NonNull User transferUser, @NonNull ExceptionTask exceptionTask,
                          @NonNull Long taskResponseId, LocalDateTime otherTime);

    /**
     * 响应接受
     *
     * @param user         接受人
     * @param task         任务
     * @param ResponseId   响应id
     * @param taskResponse 异常任务响应
     * @param acceptTime   接受时间
     */
    void responseAccept(@NonNull FtUser user, @NonNull ExceptionTask task, @NonNull Long ResponseId, ExceptionTaskResponse taskResponse, LocalDateTime acceptTime);

    /**
     * 响应提交处理人
     *
     * @param user              操作人
     * @param submitHandingUser 处理人
     * @param task              任务
     * @param taskResponseId    响应id
     * @param taskResponse      异常任务响应
     */
    void responseHanding(@NonNull FtUser user, @NonNull   List<User>  submitHandingUser, @NonNull ExceptionTask task, @NonNull Long taskResponseId, ExceptionTaskResponse taskResponse);

    /**
     * 处理节点-驳回记录
     *
     * @param user           处理人
     * @param exceptionTask  异常任务
     * @param taskRecordNode 驳回节点
     * @param taskHandingId  异常处理id
     * @param acceptUserId   驳回接受人
     * @param rejectReason   驳回原因
     * @param rejectTime     驳回时间
     */
    void handingNodeReject(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, Long taskHandingId,
                           Long acceptUserId, String rejectReason, LocalDateTime rejectTime);

    /**
     * 提交处理
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param taskRecordNode       驳回节点
     * @param exceptionTaskHanding 异常处理
     */
    void handingUpdate(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, ExceptionTaskHanding exceptionTaskHanding, String recordTemplate);

    /**
     * 申请验收
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param taskRecordNode       驳回节点
     * @param exceptionTaskHanding 异常处理
     */
    void handingApplicationCheck(FtUser user, ExceptionTask exceptionTask, TaskRecordNode taskRecordNode, ExceptionTaskHanding exceptionTaskHanding, String recordTemplate);

    /**
     * 处理节点-转派记录
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param taskHandingId        异常处理id
     * @param otherUser            转派接受人
     * @param otherRemark          转派备注
     * @param exceptionTaskHanding 异常处理
     */
    void handingNodeToOther(FtUser user, ExceptionTask exceptionTask, Long taskHandingId, User otherUser, String otherRemark, ExceptionTaskHanding exceptionTaskHanding);

    /**
     * 处理节点-接受记录
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param taskHandingId        异常处理id
     * @param exceptionTaskHanding 异常处理
     */
    void handingNodeAccept(FtUser user, ExceptionTask exceptionTask, Long taskHandingId, ExceptionTaskHanding exceptionTaskHanding);

    /**
     * 处理节点-挂起记录
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param exceptionTaskHanding 异常处理
     */
    void handingNodeSuspend(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding);

    /**
     * 处理节点-挂起延期
     *
     * @param user                 处理人
     * @param exceptionTask        异常任务
     * @param exceptionTaskHanding 异常处理
     * @param reason               延期原因
     * @param resumeTime           延期恢复时间
     */
    void handingNodeSuspendDelay(FtUser user, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding, String reason, LocalDateTime resumeTime);

    /**
     * 处理节点-挂起恢复
     *
     * @param user                 处理人 可为空
     * @param isHandResume         是否手动恢复
     * @param exceptionTask        异常任务
     * @param exceptionTaskHanding 异常处理
     */
    void handingNodeSuspendResume(FtUser user, boolean isHandResume, ExceptionTask exceptionTask, ExceptionTaskHanding exceptionTaskHanding);

    /**
     * 处理节点-异常协同创建
     *
     * @param user                  处理人
     * @param exceptionTask         异常任务
     * @param recordId              履历id
     * @param planUserName          计划协同人
     * @param createTime            创建时间
     * @param cooperateExtendFields 附加字段
     */
    void handingNodeCooperationCreate(FtUser user, ExceptionTask exceptionTask, Long recordId, String planUserName,
                                      Date createTime, List<FormFieldResponse> cooperateExtendFields);

    /**
     * 验收通过记录
     *
     * @param user          处理人
     * @param exceptionTask 异常任务
     * @param check         验收信息
     * @param submitTime    验收时间
     */
    void checkPass(FtUser user, ExceptionTask exceptionTask, ExceptionTaskCheck check, LocalDateTime submitTime, String recordTemplate);

    /**
     * 验收驳回记录
     *
     * @param user         处理人
     * @param rejectReason 驳回原因
     * @param task         异常任务
     * @param check        验收信息
     * @param rejectTime   驳回时间
     */
    void checkReject(FtUser user, String rejectReason, ExceptionTask task, ExceptionTaskCheck check, LocalDateTime rejectTime, String recordTemplate);

    /**
     * 异常协同-提交记录
     *
     * @param user            操作人
     * @param task            异常任务
     * @param taskCooperation 异常协同
     */
    void cooperationNodeSubmit(FtUser user, ExceptionTask task, ExceptionTaskCooperation taskCooperation);

    /**
     * 异常协同-转派记录
     *
     * @param user            操作人
     * @param task            异常任务
     * @param taskCooperation 异常协同
     * @param transferUser    转派人
     */
    void cooperationNodeTransfer(FtUser user, ExceptionTask task, ExceptionTaskCooperation taskCooperation, User transferUser);

    /**
     * 异常协同-撤销记录
     *
     * @param user            操作人
     * @param task            异常任务
     * @param taskCooperation 异常协同
     */
    void cooperationNodeCancel(FtUser user, ExceptionTask task, ExceptionTaskCooperation taskCooperation);

    /**
     * 异常协同-接收记录
     *
     * @param user            操作人
     * @param task            异常任务
     * @param taskCooperation 异常协同
     */
    void cooperationNodeAccept(FtUser user, ExceptionTask task, ExceptionTaskCooperation taskCooperation);

    /**
     * 异常定时超时消息记录
     *
     * @param sendResponse  消息待发送数据
     * @param recordNode    履历节点
     * @param recordType    履历类型
     * @param isCooperation 是否是协同超时消息
     */
    void timeOutMessage(MessageToBeSendResponse sendResponse, TaskRecordNode recordNode, TaskRecordType recordType, Boolean isCooperation);

    /**
     * 前12天每天挂起数量
     *
     * @param type      类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsDayTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 前12天每周挂起数量
     *
     * @param type      类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsWeekTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 前12天每月挂起数量
     *
     * @param type      类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsMonthTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 每年挂起数量
     *
     * @param type      类型
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 数量和日期
     */
    List<StatisticsTrendValueResponse> statisticsYearTrend(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 每天响应超时次数
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
     * 前12天每周超时次数
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
    List<StatisticsTrendValueResponse> statisticsWeekTimeout(String type, LocalDateTime startDate, LocalDateTime endDate, List<Long> deptIds, Long exceptionCategoryId, Long exceptionProcessId, Long workspaceLocationId);

    /**
     * 前12天每月超时次数
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


}

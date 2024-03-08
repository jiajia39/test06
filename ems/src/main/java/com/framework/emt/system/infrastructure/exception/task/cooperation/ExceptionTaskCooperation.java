package com.framework.emt.system.infrastructure.exception.task.cooperation;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationCreateRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationSubmitRequest;
import com.framework.emt.system.domain.task.cooperation.request.TaskCooperationTransferRequest;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationSubStatus;
import com.framework.emt.system.infrastructure.exception.task.handing.ExceptionTaskHanding;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 异常任务协同 实体类
 *
 * @author ds_C
 * @since 2023-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_cooperation", autoResultMap = true)
public class ExceptionTaskCooperation extends TenantEntity {

    /**
     * 协同编号
     */
    private String cooperationCode;

    /**
     * 异常任务表id
     */
    private Long exceptionTaskId;

    /**
     * 处理版本号
     */
    private Integer handingVersion;

    /**
     * 异常任务处理id
     */
    private Long exceptionTaskHandingId;

    /**
     * 协同状态
     */
    private CooperationStatus cooperationStatus;

    /**
     * 协同子状态
     */
    private CooperationSubStatus cooperationSubStatus;

    /**
     * 协同标题
     */
    private String title;

    /**
     * 超时上报流程id
     */
    private Long reportNoticeProcessId;

    /**
     * 超时上报流程名称
     */
    private String reportNoticeProcessName;

    /**
     * 处理时限 单位:分钟
     */
    private Integer durationTime;

    /**
     * 处理最后期限
     */
    private LocalDateTime finishDeadline;

    /**
     * 计划协同人id
     */
    private Long planUserId;

    /**
     * 协同/转派人id
     */
    private Long userId;

    /**
     * 转派备注
     */
    private String otherRemark;

    /**
     * 转派时间
     */
    private LocalDateTime otherTime;

    /**
     * 接受人id
     */
    private Long acceptUserId;

    /**
     * 接受时间
     */
    private LocalDateTime acceptTime;

    /**
     * 原因分析
     */
    private String submitReasonAnalysis;

    /**
     * 解决方案
     */
    private String submitSolution;

    /**
     * 处理结果
     */
    private String submitResult;

    /**
     * 提交附件列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> submitFiles;

    /**
     * 提交附加数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 撤销时间
     */
    private LocalDateTime cancelTime;

    /**
     * 异常协同-接收
     *
     * @param currentUserId 当前用户id
     * @param currentTime   当前时间
     * @return
     */
    public ExceptionTaskCooperation receiveInit(Long currentUserId, LocalDateTime currentTime) {
        this.acceptUserId = currentUserId;
        this.acceptTime = currentTime;
        this.cooperationStatus = CooperationStatus.COOPERATION_ING;
        this.cooperationSubStatus = CooperationSubStatus.COOPERATION_ING;
        return this;
    }

    /**
     * 异常协同-转派
     *
     * @param request
     * @param currentTime 当前时间
     * @return
     */
    public ExceptionTaskCooperation transferInit(TaskCooperationTransferRequest request, LocalDateTime currentTime) {
        this.userId = request.getUserId();
        this.otherRemark = request.getOtherRemark();
        this.otherTime = currentTime;
        this.cooperationStatus = CooperationStatus.WAIT_COOPERATION;
        this.cooperationSubStatus = CooperationSubStatus.ALREADY_TRANSFER;
        return this;
    }

    public ExceptionTaskCooperation cancel(LocalDateTime currentTime) {
        this.cancelTime = currentTime;
        this.cooperationStatus = CooperationStatus.ALREADY_REVOKED;
        this.cooperationSubStatus = CooperationSubStatus.ALREADY_REVOKED;
        return this;
    }

    /**
     * 异常协同-提交
     *
     * @param request
     * @param currentTime 当前时间
     * @return
     */
    public ExceptionTaskCooperation submitInit(TaskCooperationSubmitRequest request, LocalDateTime currentTime, List<FormFieldResponse> extendDataList) {
        this.submitReasonAnalysis = request.getSubmitReasonAnalysis();
        this.submitSolution = request.getSubmitSolution();
        this.submitResult = request.getSubmitResult();
        this.submitFiles = request.getSubmitFiles();
        this.cooperationStatus = CooperationStatus.ALREADY_COMPLETED;
        this.cooperationSubStatus = CooperationSubStatus.ALREADY_COMPLETED;
        this.submitExtendDatas = extendDataList;
        this.submitTime = currentTime;
        return this;
    }

    /**
     * 异常协同-创建
     *
     * @param request
     * @param exceptionTaskHanding
     * @param currentTime             当前时间
     * @param reportNoticeProcessName 超时上报流程名称
     * @param cooperateExtendFields   协同附加字段
     * @return
     */
    public ExceptionTaskCooperation createInit(TaskCooperationCreateRequest request, ExceptionTaskHanding exceptionTaskHanding,
                                               LocalDateTime currentTime, String reportNoticeProcessName, List<FormFieldResponse> cooperateExtendFields) {
        this.title = request.getTitle();
        this.reportNoticeProcessId = request.getReportNoticeProcessId();
        this.durationTime = request.getDurationTime();
        this.planUserId = request.getPlanUserId();
        this.userId = request.getPlanUserId();
        this.exceptionTaskId = exceptionTaskHanding.getExceptionTaskId();
        this.handingVersion = exceptionTaskHanding.getHandingVersion();
        this.exceptionTaskHandingId = exceptionTaskHanding.getId();
        this.cooperationStatus = CooperationStatus.WAIT_COOPERATION;
        this.cooperationSubStatus = CooperationSubStatus.WAIT_COOPERATION;
        this.reportNoticeProcessName = reportNoticeProcessName;
        this.submitExtendDatas = cooperateExtendFields;
        this.finishDeadline = LocalDateTimeUtil.offset(currentTime, request.getDurationTime(), ChronoUnit.MINUTES);
        return this;
    }

    public void updateStatus() {
        this.cooperationStatus = CooperationStatus.ALREADY_REVOKED;
        this.cooperationSubStatus = CooperationSubStatus.ALREADY_REVOKED;
    }
}

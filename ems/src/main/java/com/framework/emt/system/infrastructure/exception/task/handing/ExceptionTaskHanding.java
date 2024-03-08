package com.framework.emt.system.infrastructure.exception.task.handing;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.common.api.exception.ServiceException;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.domain.task.handing.request.HandingUpdateRequest;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 异常任务处理 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_handing", autoResultMap = true)
public class ExceptionTaskHanding extends TenantEntity {

    /**
     * 异常任务表id
     */
    private Long exceptionTaskId;

    /**
     * 处理版本号
     */
    private Integer handingVersion;

    /**
     * 计划处理人id
     */
    private Long planUserId;

    /**
     * 处理/转派人id
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
     * 挂起时间
     */
    private LocalDateTime suspendTime;

    /**
     * 挂起原因
     */
    private String suspendReason;

    /**
     * 挂起次数
     */
    private Integer suspendNum;

    /**
     * 处理挂起总时长
     */
    private Long suspendTotalSecond;

    /**
     * 挂起附加字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> suspendExtendDatas;

    /**
     * 挂起附件列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> suspendFiles;

    /**
     * 恢复方式 0:未恢复 1:自动 2:手动
     */
    private TaskResumeType resumeType;

    /**
     * 预计恢复时间
     */
    private LocalDateTime resumeTime;

    /**
     * 实际恢复时间
     */
    private LocalDateTime resumeRealTime;

    /**
     * 提交处理人id
     */
    private Long submitHandingUserId;

    /**
     * 原因分析
     */
    private String reasonAnalysis;

    /**
     * 解决方案
     */
    private String solution;

    /**
     * 处理结果
     */
    private String result;
    /**
     * 异常原因项列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TagInfo> reasonItems;

    /**
     * 提交附加数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> extendDatas;

    /**
     * 提交附件列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> files;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 驳回次数
     */
    private Integer rejectNum;

    /**
     * 驳回节点 0:未驳回 1:响应 2:处理 3:验收
     */
    private TaskRejectNode rejectNode;

    /**
     * 驳回来源id
     */
    private Long rejectSourceId;

    /**
     * 驳回时间
     */
    private LocalDateTime rejectTime;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 驳回人
     */
    private Long rejectUserId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 协同转派次数
     */
    private Integer cooperationOtherCount;


    // 获取挂起预计总秒数
    public long getSuspendMinutes() {
        return LocalDateTimeUtil.between(suspendTime, resumeTime, ChronoUnit.SECONDS);
    }


    // 异常处理初始化数据
    public void init(Long exceptionTaskId, Integer handingVersion, Long planUserId, List<FormFieldResponse> handingExtendFields, List<FormFieldResponse> pendingExtendFields) {
        this.exceptionTaskId = exceptionTaskId;
        this.handingVersion = handingVersion;
        this.planUserId = planUserId;
        this.userId = planUserId;
        this.rejectNode = TaskRejectNode.INIT;
        this.resumeType = TaskResumeType.INIT;
        this.reasonItems = Collections.emptyList();
        this.extendDatas = handingExtendFields;
        suspendExtendDatas = pendingExtendFields;
    }

    // 异常处理转
    public void toOtherUser(Long toUserId, String otherRemark, LocalDateTime otherTime) {
        this.userId = toUserId;
        this.otherRemark = otherRemark;
        this.otherTime = otherTime;
    }

    // 异常处理接受
    public void accept(Long acceptUserId, LocalDateTime acceptTime) {
        this.acceptUserId = acceptUserId;
        this.acceptTime = acceptTime;
    }

    // 异常处理挂起
    public void suspend(String reason, LocalDateTime suspendTime, LocalDateTime resumeTime, List<FileRequest> suspendFiles, List<FormFieldResponse> suspendExtendData) {
        this.suspendReason = reason;
        this.suspendTime = suspendTime;
        this.resumeTime = resumeTime;
        this.suspendFiles = suspendFiles;
        this.suspendExtendDatas = suspendExtendData;
        this.suspendNum++;
    }

    // 异常处理挂起延期
    public void suspendDelay(LocalDateTime resumeTime) {
        this.resumeTime = resumeTime;
    }

    // 异常处理挂起恢复
    public long resume(TaskResumeType resumeType, LocalDateTime resumeTime) {
        this.resumeType = resumeType;
        this.resumeRealTime = resumeTime;
        long suspendSeconds = LocalDateTimeUtil.between(suspendTime, resumeRealTime, ChronoUnit.SECONDS);
        suspendTotalSecond += suspendSeconds;

        return suspendSeconds;
    }

    public void update(HandingUpdateRequest submitRequest, Long userId, List<FormFieldResponse> extendDataList) {
        this.reasonItems = submitRequest.getReasonItems();
        this.reasonAnalysis = submitRequest.getSubmitReasonAnalysis();
        this.solution = submitRequest.getSubmitSolution();
        this.result = submitRequest.getSubmitResult();
        this.files = submitRequest.getSubmitFiles();
        this.extendDatas = extendDataList;
        this.submitHandingUserId = userId;
    }

    public void validateSubmit() {
        if (CollectionUtil.isEmpty(reasonItems)) {
            throw new ServiceException("处理信息未更新,无法申请验收");
        }
    }

    // 异常处理提交验收
    public Long submit(LocalDateTime submitTime) {
        this.submitTime = submitTime;
        LocalDateTime createTime = LocalDateTime.ofInstant(getCreateTime().toInstant(), ZoneId.systemDefault());
        Long handingSeconds = LocalDateTimeUtil.between(createTime, submitTime, ChronoUnit.SECONDS);
        // 获取异常处理实际处理时间
        return handingSeconds - suspendTotalSecond;
    }

    public void copy(Integer handingVersion) {
        this.setId(null);
        this.extendDatas = new ArrayList<>();
        this.files = new ArrayList<>();
        this.submitHandingUserId = null;
        this.submitTime = null;
        this.handingVersion = handingVersion;
    }

    //验收驳回
    public void reject(Long rejectSourceId, LocalDateTime rejectTime, String rejectReason, Long rejectUserId) {
        this.rejectNode = TaskRejectNode.CHECK;
        this.rejectSourceId = rejectSourceId;
        this.rejectTime = rejectTime;
        this.rejectReason = rejectReason;
        this.rejectUserId = rejectUserId;
    }

    //更新协同转派次数
    public ExceptionTaskHanding updateCooVer() {
        this.cooperationOtherCount++;
        return this;
    }

    //自动更新挂起信息
    public long updateSuspendByHanding(LocalDateTime resumeRealTime) {
        this.resumeType = TaskResumeType.AUTO;
        this.resumeRealTime = resumeRealTime;
        long suspendSeconds = LocalDateTimeUtil.between(suspendTime, resumeRealTime, ChronoUnit.SECONDS);
        suspendTotalSecond += suspendSeconds;
        return suspendSeconds;
    }
}

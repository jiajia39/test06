package com.framework.emt.system.infrastructure.exception.task.submit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常任务提报表 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_submit", autoResultMap = true)
public class ExceptionTaskSubmit extends TenantEntity {

    /**
     * 异常任务id
     */
    private Long exceptionTaskId;

    /**
     * 提报版本号 重新提报自增1
     */
    private Integer submitVersion;

    /**
     * 提报部门id
     */
    private Long deptId;

    /**
     * 提报创建人
     */
    private Long userId;

    /**
     * 异常流程id
     */
    private Long exceptionProcessId;

    /**
     * 异常流程名称
     */
    private String exceptionProcessTitle;

    /**
     * 异常分类id
     */
    private Long exceptionCategoryId;

    /**
     * 异常项id
     */
    private Long exceptionItemId;

    /**
     * 紧急程度 0:轻微 1:一般 2:紧急 3:特急
     */
    private PriorityEnum priority;

    /**
     * 严重程度 0:轻微 1:一般 2:严重 3:致命
     */
    private SeverityEnum severity;

    /**
     * 响应时限 单位:分钟
     */
    private Integer responseDurationTime;

    /**
     * 处理时限 单位:分钟
     */
    private Integer handingDurationTime;

    /**
     * 作业单元id
     */
    private Long workspaceLocationId;

    /**
     * 故障时间
     */
    private LocalDateTime faultTime;

    /**
     * 问题描述
     */
    private String problemDesc;

    /**
     * 提报响应人id
     */
    private Long submitResponseUserId;


    /**
     * 提报附件列表 JSON字段配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> submitFiles = new ArrayList<>();

    /**
     * 提交附加数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas = new ArrayList<>();

    /**
     * 验收成功通知人
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> noticeUserIds = new ArrayList<>();

    /**
     * 提报时间
     */
    private LocalDateTime submitTime;

    /**
     * 提报人id
     */
    private Long submitUserId;

    /**
     * 驳回节点 0:未驳回 1:响应 2:处理
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
     * 撤销时间
     */
    private LocalDateTime cancelTime;


    public void submit(LocalDateTime submitTime, Long submitUserId, Integer submitVersion) {
        this.submitTime = submitTime;
        this.submitUserId = submitUserId;
        this.submitVersion = submitVersion;
    }

    // 驳回信息
    public void reject(TaskRejectNode rejectNode, Long rejectSourceId, LocalDateTime rejectTime, String rejectReason, Long rejectUserId) {
        this.rejectNode = rejectNode;
        this.rejectSourceId = rejectSourceId;
        this.rejectTime = rejectTime;
        this.rejectReason = rejectReason;
        this.rejectUserId = rejectUserId;
        this.submitVersion++;
    }

    /**
     * 提报撤销
     */
    public ExceptionTaskSubmit cancel(LocalDateTime currentTime) {
        cancelTime = currentTime;
        return this;
    }

    /**
     * 初始化 复制提报信息
     */
    public void init(Integer submitVersion) {
        this.setId(null);
        this.submitVersion = submitVersion;
        this.submitTime = null;
        this.submitUserId = null;
    }

}

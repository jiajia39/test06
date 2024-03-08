package com.framework.emt.system.infrastructure.exception.task.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务响应 实体类
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_exception_task_response", autoResultMap = true)
public class ExceptionTaskResponse extends TenantEntity {

    /**
     * 异常任务表id
     */
    private Long exceptionTaskId;

    /**
     * 响应版本号
     */
    private Integer responseVersion;
    /**
     * 计划响应人id
     */
    private Long planUserId;

    /**
     * 响应/转派人id
     */
    private Long userId;

    /**
     * 转派备注
     */
    private String otherRemark;

    /**
     * 接受人id
     */
    private Long acceptUserId;

    /**
     * 接受时间
     */
    private LocalDateTime acceptTime;

    /**
     * 提交处理人id
     */
    private Long submitHandingUserId;

    /**
     * 提交附加数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas;
    /**
     * 转派时间
     */
    private LocalDateTime otherTime;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 驳回次数
     */
    private Integer rejectNum;

    /**
     * 驳回节点
     */
    private TaskRejectNode rejectNode;

    /**
     * 驳回来源
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
     * 响应驳回信息
     *
     * @param rejectSourceId 驳回来源id
     * @param rejectTime     驳回时间
     * @param rejectReason   驳回原因
     * @param rejectUserId   驳回人
     */
    public void reject(Long rejectSourceId, LocalDateTime rejectTime, String rejectReason, Long rejectUserId) {
        this.rejectNode = TaskRejectNode.HANDING;
        this.rejectSourceId = rejectSourceId;
        this.rejectTime = rejectTime;
        this.rejectReason = rejectReason;
        this.rejectUserId = rejectUserId;
    }

    public void createResponse(Long exceptionTaskId, Integer responseVersion, Long planUserId, List<FormFieldResponse> responseExtendFields) {
        this.exceptionTaskId = exceptionTaskId;
        this.responseVersion = responseVersion;
        this.planUserId = planUserId;
        this.userId = planUserId;
        this.rejectNode = TaskRejectNode.INIT;
        this.submitExtendDatas = responseExtendFields;
    }

    public ExceptionTaskResponse submit(List<FormFieldResponse> submitExtendData, LocalDateTime submitTime) {
        this.submitHandingUserId = 0L;
        this.submitExtendDatas = submitExtendData;
        this.submitTime = submitTime;
        return this;
    }

    /**
     * 初始化 复制响应信息
     */
    public void init(Integer responseVersion) {
        this.setId(null);
        this.responseVersion = responseVersion;
        this.submitTime = null;
    }

    public ExceptionTaskResponse handingAccept(Long userId) {
        this.submitHandingUserId = userId;
        return this;
    }

}

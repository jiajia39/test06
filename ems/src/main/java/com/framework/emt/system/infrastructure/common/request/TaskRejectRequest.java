package com.framework.emt.system.infrastructure.common.request;

import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 异常驳回请求参数
 *
 * @author jiaXue
 * date 2023/8/18
 */
@Setter
@Getter
public class TaskRejectRequest {

    @ApiModelProperty(value = "驳回节点")
    private TaskRejectNode taskRejectNode;

    @ApiModelProperty(value = "驳回来源id")
    private Long sourceId;

    @ApiModelProperty(value = "驳回时间")
    private LocalDateTime rejectTime;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "驳回人id")
    private Long rejectUserId;

    public TaskRejectRequest(TaskRejectNode taskRejectNode, Long sourceId,
                             LocalDateTime rejectTime, String rejectReason, Long rejectUserId) {
        this.taskRejectNode = taskRejectNode;
        this.sourceId = sourceId;
        this.rejectTime = rejectTime;
        this.rejectReason = rejectReason;
        this.rejectUserId = rejectUserId;
    }
}

package com.framework.emt.system.infrastructure.exception.task.record.response;

import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常任务履历-驳回历史 响应体
 *
 * @author ds_C
 * @since 2023-08-21
 */
@Getter
@Setter
public class RejectResponse implements Serializable {

    @ApiModelProperty(value = "异常任务记录id")
    private Long id;

    @ApiModelProperty(value = "履历id")
    private Long recordId;

    @ApiModelProperty(value = "异常任务id")
    private Long taskId;

    @ApiModelProperty(value = "驳回节点 0:未驳回 1:响应 2:处理 3:验收")
    private TaskRejectNode rejectNode;

    @ApiModelProperty(value = "驳回节点名称")
    private String rejectNodeName;

    @ApiModelProperty(value = "操作类型")
    private TaskRecordType recordType;

    @ApiModelProperty(value = "驳回人id")
    private Long rejectUserId;

    @ApiModelProperty(value = "驳回人姓名")
    private String rejectUserName;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "驳回时间")
    private Date rejectTime;

}

package com.framework.emt.system.infrastructure.exception.task.record.response;

import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常任务履历-转派历史 响应体
 *
 * @author ds_C
 * @since 2023-08-21
 */
@Getter
@Setter
public class TransferResponse implements Serializable {

    @ApiModelProperty(value = "异常任务履历id")
    private Long id;

    @ApiModelProperty(value = "履历id")
    private Long recordId;

    @ApiModelProperty(value = "异常任务id")
    private Long taskId;

    @ApiModelProperty(value = "转派节点")
    private TaskRecordNode transferNode;

    @ApiModelProperty(value = "操作类型")
    private TaskRecordType recordType;

    @ApiModelProperty(value = "转派人id")
    private Long transferUserId;

    @ApiModelProperty(value = "转派人名称")
    private String transferUserName;

    @ApiModelProperty(value = "转派信息")
    private String transferInformation;

    @ApiModelProperty(value = "转派时间")
    private String transferTime;

}

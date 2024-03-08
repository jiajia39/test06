package com.framework.emt.system.domain.task.handing.response;

import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 异常处理-挂起历史 响应体
 *
 * @author ds_C
 * @since 2023-08-23
 */
@Getter
@Setter
public class HandingSuspendResponse implements Serializable {

    @ApiModelProperty(value = "履历id")
    private Long recordId;

    @ApiModelProperty(value = "异常处理id")
    private Long handingId;

    @ApiModelProperty(value = "履历节点")
    private TaskRecordNode recordNode;

    @ApiModelProperty(value = "操作类型")
    private TaskRecordType recordType;

    @ApiModelProperty(value = "履历任务id")
    private Long recordTaskId;

    @ApiModelProperty(value = "异常处理任务id")
    private Long handingTaskId;

    @ApiModelProperty(value = "挂起时间")
    private Date suspendTime;

    @ApiModelProperty(value = "预计恢复时间")
    private Date resumeTime;

    @ApiModelProperty(value = "实际恢复时间")
    private Date resumeRealTime;

    @ApiModelProperty(value = "挂起原因")
    private String suspendReason;

    @ApiModelProperty(value = "挂起附加")
    private List<FileRequest> suspendFiles;

}

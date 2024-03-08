package com.framework.emt.system.infrastructure.exception.task.task.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * 异常履历分页列表
 *
 * @author ds_C
 * @since 2023-08-21
 */
@Getter
@Setter
public class TaskRecordResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常履历id")
    private Long id;

    @ApiModelProperty(value = "异常任务id")
    private Long exceptionTaskId;

    @ApiModelProperty(value = "履历内容")
    private String recordContent;

    @ApiModelProperty(value = "履历类型")
    private Integer recordType;

    @ApiModelProperty(value = "异常类型")
    private Integer exceptionType;

    /**
     * 初始化响应体
     */
    public void init() {
        if (TaskRecordType.isTransfer(recordType)) {
            this.exceptionType = NumberUtils.INTEGER_ONE;
        } else if (TaskRecordType.isTimeoutRejectCancel(recordType)) {
            this.exceptionType = NumberUtils.INTEGER_TWO;
        } else {
            this.exceptionType = NumberUtils.INTEGER_ZERO;
        }
    }

}

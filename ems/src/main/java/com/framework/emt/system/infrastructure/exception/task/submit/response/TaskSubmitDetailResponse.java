package com.framework.emt.system.infrastructure.exception.task.submit.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常提报详情 响应体
 *
 * @author ds_C
 * @since 2023-08-31
 */
@Getter
@Setter
public class TaskSubmitDetailResponse implements Serializable {

    @ApiModelProperty(value = "异常提报id")
    private Long id;

    @ApiModelProperty(value = "异常提报任务id")
    private Long taskId;

    @ApiModelProperty(value = "异常提报状态")
    private Integer status;

    @ApiModelProperty(value = "异常提报子状态")
    private Integer subStatus;

    @ApiModelProperty(value = "异常提报版本")
    private Integer version;

}

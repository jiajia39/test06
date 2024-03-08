package com.framework.emt.system.infrastructure.exception.task.schedule.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常任务定时计划 响应体
 *
 * @author ds_C
 * @since 2023-08-26
 */
@Getter
@Setter
public class TaskScheduleResponse implements Serializable {

    @ApiModelProperty(value = "异常任务定时计划id")
    private String ids;

    @ApiModelProperty(value = "超时时限 单位：分钟")
    private Integer processTimeLimit;

    @ApiModelProperty(value = "执行状态 0:未执行 1:已执行 2.已取消")
    private Integer executeStatus;

    @ApiModelProperty(value = "上报人")
    private String reportUserNames;

}

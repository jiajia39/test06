package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计实时信息 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsRealTimeResponse implements Serializable {
    @ApiModelProperty(value = "待响应数量")
    private Integer totalPreResponseQuantity;

    @ApiModelProperty(value = "待处理数量")
    private Integer totalPreHandingQuantity;

    @ApiModelProperty(value = "超时中数量")
    private Integer totalTimeoutQuantity;

    @ApiModelProperty(value = "挂起中数量")
    private Integer totalPendingQuantity;
}

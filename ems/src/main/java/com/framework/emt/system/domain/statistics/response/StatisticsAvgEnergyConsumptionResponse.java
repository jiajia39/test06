package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常平均耗能对比 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsAvgEnergyConsumptionResponse implements Serializable {
    @ApiModelProperty(value = "平均响应时间")
    private List<StatisticsAvgDetailResponse> avgResponseTime;

    @ApiModelProperty(value = "平均处理时间")
    private List<StatisticsAvgDetailResponse> avgHandingTime;

    @ApiModelProperty(value = "平均验收时间")
    private List<StatisticsAvgDetailResponse> avgCheckTime;
}

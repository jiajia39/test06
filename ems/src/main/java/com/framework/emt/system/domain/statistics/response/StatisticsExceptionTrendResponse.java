package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常趋势折线图 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsExceptionTrendResponse implements Serializable {
    @ApiModelProperty(value = "提报数据")
    private List<StatisticsTrendValueResponse> submitData;

    @ApiModelProperty(value = "异常关闭")
    private List<StatisticsTrendValueResponse> finishData;

    @ApiModelProperty(value = "响应超时次数")
    private List<StatisticsTrendValueResponse> responseTimeOutCount;

    @ApiModelProperty(value = "处理超时次数")
    private List<StatisticsTrendValueResponse> handingTimeOutCount;
}

package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 统计实时信息 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsTrendTimeoutResponse implements Serializable {
    @ApiModelProperty(value = "响应超时次数")
    private List<StatisticsTrendValueResponse> responseTimeOutCount;

    @ApiModelProperty(value = "处理超时次数")
    private List<StatisticsTrendValueResponse> handingTimeOutCount;

    @ApiModelProperty(value = "协同超时次数")
    private List<StatisticsTrendValueResponse> cooperationTimeOutCount;

}

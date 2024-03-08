package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 占比饼图 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsProportionPieResponse implements Serializable {
    @ApiModelProperty(value = "饼图外环数据")
    private List<StatisticsProportionResponse> outerRingDate;

    @ApiModelProperty(value = "异常共计")
    private Integer exceptionTotal;

    public void init(List<StatisticsProportionResponse> outerRingDate, Integer exceptionTotal) {
        this.outerRingDate = outerRingDate;
        this.exceptionTotal = exceptionTotal;
    }
}

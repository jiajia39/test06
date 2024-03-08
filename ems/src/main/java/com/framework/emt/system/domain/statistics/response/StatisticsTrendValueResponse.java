package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsTrendValueResponse implements Serializable {
    @ApiModelProperty(value = "值")
    private Integer value;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "日期范围")
    private String dateRange;
}

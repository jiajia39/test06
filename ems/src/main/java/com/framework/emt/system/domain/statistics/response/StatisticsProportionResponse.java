package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 占比 查询结果
 *
 * @author gaojia
 * @since 2023-08-24
 */
@Getter
@Setter
public class StatisticsProportionResponse implements Serializable {
    @ApiModelProperty(value = "个数")
    private Integer count;

    @ApiModelProperty(value = "名称")
    private String name;
}

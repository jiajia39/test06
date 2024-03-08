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
public class StatisticsTrendResponse implements Serializable {
    @ApiModelProperty(value = "提报数据")
    private List<StatisticsTrendValueResponse> submitData;

    @ApiModelProperty(value = "异常关闭")
    private List<StatisticsTrendValueResponse> finishData;

    @ApiModelProperty(value = "异常挂起")
    private List<StatisticsTrendValueResponse> pendingData;

}

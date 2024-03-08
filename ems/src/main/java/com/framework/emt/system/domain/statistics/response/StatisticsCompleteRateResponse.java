package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计关闭率 查询结果
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsCompleteRateResponse implements Serializable {
    @ApiModelProperty(value = "本日提报次数")
    private Integer daySubmitCount = 0;
    @ApiModelProperty(value = "本周提报次数")
    private Integer weekSubmitCount = 0;
    @ApiModelProperty(value = "本月提报次数")
    private Integer monthSubmitCount = 0;


    @ApiModelProperty(value = "本日完成次数")
    private Integer dayFinishCount = 0;
    @ApiModelProperty(value = "本周完成次数")
    private Integer weekFinishCount = 0;
    @ApiModelProperty(value = "本月完成次数")
    private Integer monthFinishCount = 0;

}

package com.framework.emt.system.domain.statistics.response;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计汇总信息 查询结果
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsSummaryResponse implements Serializable {
    @ApiModelProperty(value = "总提报数量")
    private Integer totalSubmitQuantity;

    @ApiModelProperty(value = "总关闭数量")
    private Integer totalFinishQuantity;

    @ApiModelProperty(value = "总超时数量")
    private Integer totalTimeoutQuantity;

    @ApiModelProperty(value = "总挂起数量")
    private Integer totalPendingQuantity;
}

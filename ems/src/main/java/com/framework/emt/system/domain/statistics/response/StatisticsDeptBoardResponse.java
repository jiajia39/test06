package com.framework.emt.system.domain.statistics.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计部门看板汇总信息 查询结果
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsDeptBoardResponse implements Serializable {
    @ApiModelProperty(value = "待响应数量")
    private Integer preResponseQuantity;

    @ApiModelProperty(value = "待处理数量")
    private Integer preHandingQuantity;

    @ApiModelProperty(value = "待协同数量")
    private Integer preCooperationQuantity;

    @ApiModelProperty(value = "待验收数量")
    private Integer checkQuantity;

    @ApiModelProperty(value = "超时数量")
    private Integer timeoutQuantity;

    @ApiModelProperty(value = "挂起数量")
    private Integer suspendQuantity;
}

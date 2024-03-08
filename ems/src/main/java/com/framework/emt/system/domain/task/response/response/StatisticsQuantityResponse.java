package com.framework.emt.system.domain.task.response.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * app端查询每个阶段的数量
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsQuantityResponse implements Serializable {

    @ApiModelProperty(value = "待提报数量")
    private Long preSubmitQuantity;

    @ApiModelProperty(value = "待响应数量")
    private Long preResponseQuantity;

    @ApiModelProperty(value = "待处理数量")
    private Long preHandingQuantity;

    @ApiModelProperty(value = "待协同数量")
    private Long preCooperationQuantity;

    @ApiModelProperty(value = "待验收数量")
    private Long checkQuantity;

}

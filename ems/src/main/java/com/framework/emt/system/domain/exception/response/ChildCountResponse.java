package com.framework.emt.system.domain.exception.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常分类子项数量 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ChildCountResponse implements Serializable {

    @ApiModelProperty(value = "子异常分类数量")
    private Long childrenCount;

    @ApiModelProperty(value = "异常项数量")
    private Long exceptionItemCount;

    @ApiModelProperty(value = "异常流程数量")
    private Long exceptionProcessCount;

}

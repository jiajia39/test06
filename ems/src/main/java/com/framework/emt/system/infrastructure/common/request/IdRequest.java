package com.framework.emt.system.infrastructure.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 主键id request
 *
 * @author ds_C
 * @since 2023-07-11
 */
@Getter
@Setter
public class IdRequest implements Serializable {

    @NotNull(message = "主键id不能为空")
    @ApiModelProperty(value = "主键id", required = true)
    private Long id;

}

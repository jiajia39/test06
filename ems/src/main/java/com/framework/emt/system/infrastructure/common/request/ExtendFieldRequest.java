package com.framework.emt.system.infrastructure.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 附加字段 响应体
 *
 * @author ds_C
 * @since 2023-08-07
 */
@Getter
@Setter
public class ExtendFieldRequest implements Serializable {

    @NotNull(message = "附加字段id不能为空")
    @ApiModelProperty(value = "附加字段id")
    private Long id;

    @ApiModelProperty(value = "附加字段是否必填")
    private Boolean required;

    public ExtendFieldRequest() {
        this.required = false;
    }

}

package com.framework.emt.system.domain.formfield.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 选择框选项 创建参数
 *
 * @author gaojia
 * @since 2023-07-28
 */
@Getter
@Setter
public class SelectListRequest implements Serializable {
    @NotNull(message = "选择框选项key不能为空")
    @ApiModelProperty(value = "选择框选项key", required = true)
    private Integer key;

    @NotNull(message = "选择框选项value不能为空")
    @ApiModelProperty(value = "选择框选项value", required = true)
    private String value;

}

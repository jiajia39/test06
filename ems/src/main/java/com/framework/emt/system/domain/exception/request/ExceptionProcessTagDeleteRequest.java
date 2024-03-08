package com.framework.emt.system.domain.exception.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常流程标签 删除参数
 *
 * @author ds_C
 * @since 2023-08-03
 */
@Getter
@Setter
public class ExceptionProcessTagDeleteRequest implements Serializable {

    @NotNull(message = "异常流程id不能为空")
    @ApiModelProperty(value = "异常流程id", required = true)
    private Long id;

    @NotNull(message = "异常原因项标签id不能为空")
    @ApiModelProperty(value = "异常原因项标签id", required = true)
    private Long tagId;

}

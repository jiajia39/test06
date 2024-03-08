package com.framework.emt.system.infrastructure.exception.task.task.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 附加字段请求参数
 *
 * @author jiaxue
 * @since 2023-08-16
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"prop"})
public class ExtendFieldsRequest implements Serializable {

    @NotBlank(message = "字段名不能为空")
    @Length(max = 50, message = "字段名长度限制{max}")
    @ApiModelProperty(value = "字段名", required = true)
    private String prop;

    @NotNull(message = "数据不能为空")
    @Length(max = 500, message = "字段值长度限制{max}")
    @ApiModelProperty(value = "字段值")
    private String key;

    @ApiModelProperty(value = "字段值")
    private String value;

}

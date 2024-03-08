package com.framework.emt.system.domain.tag.request;

import com.framework.emt.system.domain.formfield.constant.enums.FormFieldTypeEnum;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常标签 创建参数
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagCreateRequest implements Serializable {

    @NotBlank(message = "内容不能为空")
    @Length(max = 20, message = "内容长度限制{max}")
    @ApiModelProperty(value = "内容", required = true)
    private String content;
    @NotNull(message = "标签类型不能为空")
    @EnumValidator(enumClazz = TagTypeEnum.class, message = "标签类型 0知识库标签 1 异常原因项")
    @ApiModelProperty(value = "标签类型 0知识库标签 1 异常原因项", required = true)
    private Integer type;
    
}

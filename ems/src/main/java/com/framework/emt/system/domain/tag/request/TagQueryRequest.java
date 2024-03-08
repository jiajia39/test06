package com.framework.emt.system.domain.tag.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常标签 查询条件
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "内容长度限制{max}")
    @ApiModelProperty(value = "内容")
    private String content;


    @EnumValidator(enumClazz = TagTypeEnum.class, message = "标签类型 0知识库标签 1 异常原因项")
    @ApiModelProperty(value = "标签类型 0知识库标签 1 异常原因项")
    private Integer type;
}

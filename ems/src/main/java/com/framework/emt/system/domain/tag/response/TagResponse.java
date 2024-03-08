package com.framework.emt.system.domain.tag.response;

import com.framework.emt.system.domain.tag.constant.enums.TagTypeEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常标签 响应体
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常表单字段id")
    private Long id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签类型 0知识库标签 1 异常原因项")
    private TagTypeEnum type;
}

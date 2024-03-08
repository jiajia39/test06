package com.framework.emt.system.domain.tag.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 异常标签 响应体
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagInfo {

    @ApiModelProperty(value = "异常原因id")
    private Long id;

    @ApiModelProperty(value = "异常原因名称")
    private String content;
}

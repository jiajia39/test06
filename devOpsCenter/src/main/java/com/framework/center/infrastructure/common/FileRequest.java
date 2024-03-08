package com.framework.center.infrastructure.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件相关参数
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class FileRequest {

    @ApiModelProperty(value = "文件类型")
    private Integer type;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "文件地址")
    private String url;

}

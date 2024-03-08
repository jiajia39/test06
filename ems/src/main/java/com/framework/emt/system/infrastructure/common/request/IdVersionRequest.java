package com.framework.emt.system.infrastructure.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * id 版本号查询参数
 *
 * @author jiaXue
 * date 2023/8/17
 */

@Getter
@Setter
public class IdVersionRequest {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "版本号")
    private String version;
}

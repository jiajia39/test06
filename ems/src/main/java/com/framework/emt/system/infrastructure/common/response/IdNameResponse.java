package com.framework.emt.system.infrastructure.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * id name 返回数据
 *
 * @author jiaXue
 * date 2023/8/17
 */
@Getter
@Setter
@AllArgsConstructor
public class IdNameResponse {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

}

package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "统一门户登录参数", description = "统一门户登录参数")
public class SsoLoginRequest {
    /**
     * 统一门户门票
     */
    @NotBlank(message = "统一门户门票不能为空！")
    @ApiModelProperty(value = "统一门户门票")
    private String token;
}

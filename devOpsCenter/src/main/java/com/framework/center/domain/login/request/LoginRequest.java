package com.framework.center.domain.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "账户登录参数", description = "账户登录参数")
public class LoginRequest implements Serializable {
    /**
     * 账户
     */
    @NotBlank(message = "账户不能为空！")
    @ApiModelProperty(value = "账户")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空！")
    @ApiModelProperty(value = "密码")
    private String password;
}

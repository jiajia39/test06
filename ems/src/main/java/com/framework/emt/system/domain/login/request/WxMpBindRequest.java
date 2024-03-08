package com.framework.emt.system.domain.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "微信公众号绑定参数", description = "微信公众号绑定参数")
public class WxMpBindRequest implements Serializable {

    @ApiModelProperty(value = "微信unionid")
    @NotBlank(message = "微信unionid不能为空")
    private String unionId;

    @ApiModelProperty(value = "微信公众号openid")
    @NotBlank(message = "微信公众号openid不能为空")
    private String openId;

    @NotBlank(message = "账户不能为空")
    @ApiModelProperty(value = "账户")
    private String account;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "租户id")
    private String tenantId = "000000";
}

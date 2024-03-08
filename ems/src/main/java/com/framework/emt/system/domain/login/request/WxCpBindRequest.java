package com.framework.emt.system.domain.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "企业微信绑定参数", description = "企业微信绑定参数")
public class WxCpBindRequest implements Serializable {

    @ApiModelProperty(value = "应用id")
    @NotBlank(message = "应用id不能为空")
    private String clientId;

    @ApiModelProperty(value = "企业微信openid")
    @NotBlank(message = "企业微信openid不能为空")
    private String openId;

    @NotBlank(message = "账户不能为空")
    @ApiModelProperty(value = "账户")
    private String account;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "租户id")
    private String tenantId = "000000";

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "手机号")
    private String phone;
}

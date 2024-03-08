package com.framework.emt.system.domain.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "钉钉绑定参数", description = "钉钉绑定参数")
public class DingTalkBindRequest implements Serializable {

    @ApiModelProperty(value = "unionid")
    @NotBlank(message = "unionid不能为空")
    private String unionId;

    @ApiModelProperty(value = "钉钉openid")
    @NotBlank(message = "钉钉openid不能为空")
    private String openId;

    @ApiModelProperty(value = "钉钉client_id")
    @NotBlank(message = "钉钉client_id不能为空")
    private String clientId;

    @NotBlank(message = "账户不能为空")
    @ApiModelProperty(value = "账户")
    private String account;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "租户id")
    private String tenantId = "000000";
}

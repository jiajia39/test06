package com.framework.emt.system.domain.login;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_third_party_info")
public class ThirdPartyInfo extends TenantEntity {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "平台类型 0 微信公众号 1 微信小程序 2 微信企业应用 2 钉钉 ")
    private Integer platformType;

    @ApiModelProperty(value = "平台用户唯一标识")
    private String unionId;

    @ApiModelProperty(value = "平台应用唯一标识")
    private String clientId;

    @ApiModelProperty(value = "应用用户唯一标识")
    private String openId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "授权状态 0 启用 1 禁用")
    private String authorizationStatus;
}

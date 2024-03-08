package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户密码同步参数", description = "用户密码同步参数")
public class UserPasswdRequest {
    @ApiModelProperty(value = "密码（MD5）")
    private String password;
    @ApiModelProperty(value = "工号")
    private String uid;
}

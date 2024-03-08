package com.framework.center.domain.sync.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Data
@ApiModel(value = "用户密码信息同步参数", description = "用户密码信息同步参数")
public class UserPasswdSyncRequest {
    @ApiModelProperty(value = "接口编号")
    private String itfid;
    @ApiModelProperty(value = "接口调用标识")
    private String sendid;
    @ApiModelProperty(value = "系统编号")
    private String sysid;
    @ApiModelProperty(value = "用户密码数据内容")
    private List<UserPasswdRequest> item;
}

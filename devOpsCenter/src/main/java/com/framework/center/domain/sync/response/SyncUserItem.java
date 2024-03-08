package com.framework.center.domain.sync.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("SpellCheckingInspection")
@Data
@ApiModel(value = "单个信息同步响应参数", description = "单个信息同步响应参数")
public class SyncUserItem {
    @ApiModelProperty(value = "接口处理状态 1：成功  0：失败")
    private String istat;
    @ApiModelProperty(value = "处理消息")
    private String message;
    @ApiModelProperty(value = "员工工号")
    private String uid;
}

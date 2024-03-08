package com.framework.center.domain.sync.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Data
@ApiModel(value = "信息同步响应参数", description = "信息同步响应参数")
public class SyncResponse<T> {
    @ApiModelProperty(value = "接口处理状态 1：成功  0：失败")
    private String istat;
    @ApiModelProperty(value = "接口编号")
    private String itfid;
    @ApiModelProperty(value = "处理消息")
    private String message;
    @ApiModelProperty(value = "接口调用标识")
    private String sendid;
    @ApiModelProperty(value = "系统编号")
    private String sysid;
    @JsonProperty("return")
    private List<T> syncItemList;

}

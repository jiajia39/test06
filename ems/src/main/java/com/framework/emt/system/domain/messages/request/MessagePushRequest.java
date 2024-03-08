package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "消息推送参数", description = "消息推送参数")
public class MessagePushRequest {
    @ApiModelProperty(value = "发送者系统编号，参考各系统编号定义")
    private String systemId;
    @ApiModelProperty(value = "该系统调用此接口的口令（由门户提供）")
    private String password;
    @ApiModelProperty(value = "系统内部哪个模块发布的消息，可以不填")
    private String module;
    @ApiModelProperty(value = "消息列表，可以发多条消息，限制每次最多2000条。")
    private List<MessageContentRequest> messages;
}

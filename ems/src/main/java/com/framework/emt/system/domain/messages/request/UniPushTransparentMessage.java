package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "uniPush透传消息", description = "uniPush透传消息")
public class UniPushTransparentMessage implements Serializable {
    @ApiModelProperty(value = "uniPush透传内容")
    private String text;
}

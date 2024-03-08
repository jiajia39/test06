package com.framework.emt.system.domain.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(value = "uniPush消息推送", description = "uniPush消息推送")
public class UniPushMessageRequest implements Serializable {
    @ApiModelProperty(value = "接收消息的用户")
    private String uid;
    @ApiModelProperty(value = "消息标题")
    private String title;
    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息内容")
    @JsonProperty("request_id")
    private String requestId;

    @ApiModelProperty(value = "透传内容")
    private UniPushTransparentMessage data;
}

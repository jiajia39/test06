package com.framework.emt.system.domain.messages.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "消息推送结果参数", description = "消息推送结果参数")
public class MessageContentResponse {
    @ApiModelProperty(value = "是否成功")
    private String result;
    @ApiModelProperty(value = "错误消息")
    private String errInfo;
}

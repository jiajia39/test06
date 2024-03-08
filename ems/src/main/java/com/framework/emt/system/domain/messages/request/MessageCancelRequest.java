package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "挂起消息请求参数", description = "挂起消息请求参数")
public class MessageCancelRequest implements Serializable {
    @ApiModelProperty(value = "挂起消息列表")
    @NotEmpty(message = "挂起消息列表不能为空！")
    private List<MessageCancel> messageCancelList;
}

package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 消息创建请求参数
 *
 * @author yankunw
 * @since 2023-07-20
 */
@Getter
@Setter
@ApiModel(value = "消息创建请求参数", description = "消息创建请求参数")
public class MessageCreateRequest implements Serializable {

    @NotEmpty(message = "消息列表不能为空！")
    @ApiModelProperty(value = "消息列表")
    private List<MessageCreate> messageCreateList;

}

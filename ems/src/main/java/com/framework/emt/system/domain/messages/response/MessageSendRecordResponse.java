package com.framework.emt.system.domain.messages.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息推送 响应体
 *
 * @author yankunw
 * @since 2023-07-21
 */
@Getter
@Setter
@Data
@ApiModel(value = "消息发送详情数据", description = "消息发送详情数据")
public class MessageSendRecordResponse implements Serializable {

}

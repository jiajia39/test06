package com.framework.emt.system.domain.messages.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 消息发送响应数据
 * @author yankunw
 * @since 2023-07-20
 */
@Data
@ApiModel(value = "消息发送响应数据", description = "消息发送响应数据")
public class MessageSearchResponse {
    /**
     * 查询结果
     */
    private IPage<MessageResponse> page;
    /**
     * 未读数量
     */
    private Long unreadQuantity=0L;
}

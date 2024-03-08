package com.framework.emt.system.domain.messages;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_message_send_record")
public class MessageSendRecord extends TenantEntity {
    /**
     * 消息id
     */
    @ApiModelProperty(value = "消息id")
    private Long messageId;

    /**
     * 发送通道
     */
    @ApiModelProperty(value = "发送通道")
    private Integer sendChannel;

    /**
     * 发送状态
     */
    @ApiModelProperty(value = "发送状态 0:待发送 1:发送中 2:已发送 3:发送成功 4:发送失败")
    private Integer sendState;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}

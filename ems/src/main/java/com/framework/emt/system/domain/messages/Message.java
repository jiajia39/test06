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
@TableName("emt_message")
public class Message extends TenantEntity {

    /**
     * 模版id
     */
    @ApiModelProperty(value = "模版id")
    private Long messageTemplateId;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型 1.异常提报 2.异常响应 3.异常处理 4.异常协同 5.异常验收")
    private Integer businessType;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;


    /**
     * 通知级别 1.普通 2.超时预警 3.超时上报
     */
    @ApiModelProperty(value = "通知级别 1.普通 2.超时预警 3.超时上报")
    private Integer level;


    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    private Long sendUserId;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String content;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id")
    private Long receiveUserId;


    /**
     * 发送状态 0:待发送 1:发送中 2:已发送 3:发送成功 4:发送失败
     */
    @ApiModelProperty(value = "发送状态 0:待发送 1:发送中 2:已发送 3:发送成功 4:发送失败")
    private Integer sendState;

    /**
     * 定时发送时间
     */
    @ApiModelProperty(value = "定时发送时间")
    private LocalDateTime sendTime;


    /**
     * 真实发送时间
     */
    @ApiModelProperty(value = "真实发送时间")
    private LocalDateTime realSendTime;


    /**
     * 接收状态
     */
    @ApiModelProperty(value = "接收状态 0.未读 1.已读 ")
    private Integer readState=0;

    /**
     * 已读时间
     */
    @ApiModelProperty(value = "已读时间")
    private LocalDateTime readTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 批次号码
     */
    @ApiModelProperty(value = "批次号码")
    private String batchNumber;

    /**
     * 发送通道列表
     */
    @ApiModelProperty(value = "发送通道列表")
    private String sendChannels;

}

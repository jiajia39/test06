package com.framework.emt.system.domain.messages.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@ApiModel(value = "消息待发送数据", description = "消息待发送数据")
public class MessageToBeSendResponse implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "发送人账户")
    private String sendUserAccount;

    @ApiModelProperty(value = "业务类型 1.异常提报 2.异常响应 3.异常处理 4.异常协同 5.异常验收'")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "通知级别 1.普通 2.超时预警 3.超时上报")
    private Integer messageLevel;

    @ApiModelProperty(value = "消息模板id")
    private Long messageTemplateId;

    @ApiModelProperty(value = "消息模板名称")
    private String messageTemplateName;

    @ApiModelProperty(value = "消息发送时间")
    private LocalDateTime messageSendTime;

    @ApiModelProperty(value = "发送人id")
    private Long sendUserId;

    @ApiModelProperty(value = "发送人姓名")
    private String sendUserName;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "接收人id")
    private Long receiveUserId;

    @ApiModelProperty(value = "接收人姓名")
    private Long receiveUserName;

    @ApiModelProperty(value = "接收人账户")
    private String receiveUserAccount;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送状态 0:待发送 1:发送中 2:已发送 3:发送成功 4:发送失败")
    private Integer sendState;

    @ApiModelProperty(value = "发送发送通道")
    private Integer sendChannel;

}

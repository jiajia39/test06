package com.framework.emt.system.domain.messages.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "消息内容参数", description = "消息内容参数")
public class MessageContentRequest {
    @ApiModelProperty(value = "消息ID，用于跟踪和记录消息生命期，由消息发送方生成，建议格式：格式：Msg +systemID+工号+时间戳+6位随机数")
    @JsonProperty("MSG_ID")
    private String msgId;
    @ApiModelProperty(value = "对于重复发送的消息，前一条重复性消息的msgId")
    @JsonProperty("PRE_MSG_ID")
    private String preMsgId;
    @ApiModelProperty(value = "接收人唯一用户标识（唯一工号）")
    @JsonProperty("USER_ID")
    private String userId;
    @ApiModelProperty(value = "消息标题")
    @JsonProperty("MSG_TITLE")
    private String msgTitle;
    @ApiModelProperty(value = "消息内容")
    @JsonProperty("MSG_CONTENT")
    private String msgContent;
    @ApiModelProperty(value = "发送者姓名")
    @JsonProperty("SENDER_NAME")
    private String senderName;
    @ApiModelProperty(value = "内容链接地址")
    @JsonProperty("REF_URL")
    private String refUrl;
    @ApiModelProperty(value = "消息发生日期时间（格式：yyyy-MM-dd hh:mm:ss  24小时制）")
    @JsonProperty("MSG_TIME")
    private String msgTime;
    @ApiModelProperty(value = "通知目标列表：门户（1），移动门户（2），邮件（3），微信（4），短信（5），多个目标以逗号分隔，空缺缺省为门户")
    @JsonProperty("TARGET")
    private String target;
    @ApiModelProperty(value = "消息扩展内容")
    @JsonProperty("EXT_DATA")
    private String extData;
}

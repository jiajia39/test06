package com.framework.emt.system.domain.messages.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 消息推送 响应体
 *
 * @author yankunw
 * @since 2023-07-20
 */
@Data
@ApiModel(value = "消息发送数据", description = "消息发送数据")
public class MessageResponse implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 创建部门
     */
    @ApiModelProperty("创建部门")
    private Long createDept;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUser;

    /**
     * 更新人姓名
     */
    @ApiModelProperty("更新人姓名")
    private String updateUserName;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 模版id
     */
    @ApiModelProperty(value = "模版id")
    private Long messageTemplateId;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型 1.异常任务 2.协同任务 3.验收任务")
    private Integer businessType;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    private Long sendUserId;


    /**
     * 发送人姓名
     */
    @ApiModelProperty(value = "发送人姓名")
    private String sendUserName;


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
     * 接收人姓名
     */
    @ApiModelProperty(value = "接收人姓名")
    private String receiveUserName;


    /**
     * 发送状态
     */
    @ApiModelProperty(value = "发送状态 0:待发送 1:已发送")
    private Integer sendState;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;
    /**
     * 接收状态
     */
    @ApiModelProperty(value = "接收状态 0.未读 1.已读 ")
    private Integer readState;

    /**
     * 已读时间
     */
    @ApiModelProperty(value = "已读时间")
    private Date readTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private  String phone;

    /**
     * 真实发送时间
     */
    @ApiModelProperty(value = "真实发送时间")
    private LocalDateTime realSendTime;

    /**
     * 通知级别 1.普通 2.超时预警 3.超时上报
     */
    @ApiModelProperty(value = "通知级别 1.普通 2.超时预警 3.超时上报")
    private Integer level;

}

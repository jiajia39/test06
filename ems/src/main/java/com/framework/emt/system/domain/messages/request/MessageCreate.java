package com.framework.emt.system.domain.messages.request;

import com.framework.emt.system.domain.messages.constant.enums.ExceptionType;
import com.framework.emt.system.domain.messages.constant.enums.NoticeLevel;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "消息创建参数", description = "消息创建参数")
public class MessageCreate implements Serializable {

    /**
     * 模版id
     */
    @NotNull(message = "模版id不能为空！")
    @ApiModelProperty(value = "模版id")
    private Long messageTemplateId;

    @ApiModelProperty(value = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空！")
    @EnumValidator(enumClazz = ExceptionType.class, message = "业务类型错误 1.异常提报 2.异常响应 3.异常处理 4.异常协同 5.异常验收")
    @ApiModelProperty(value = "业务类型 1.异常提报 2.异常响应 3.异常处理 4.异常协同 5.异常验收")
    private Integer businessType;

    /**
     * 业务id
     */
    @NotNull(message = "业务id不能为空！")
    @ApiModelProperty(value = "业务id")
    private Long businessId;


    /**
     * 通知级别 1.普通 2.超时预警 3.超时上报
     */
    @EnumValidator(enumClazz = NoticeLevel.class, message = "通知级别错误 1.普通 2.超时预警 3.超时上报")
    @ApiModelProperty(value = "通知级别 1.普通 2.超时预警 3.超时上报")
    private Integer level;

    /**
     * 发送人id
     */
    @ApiModelProperty(value = "发送人id")
    private Long sendUserId;


    /**
     * 接收人id
     */
    @NotEmpty(message = "接收人不能为空！")
    @ApiModelProperty(value = "接收人id")
    private Long receiveUserId;

    /**
     * 定时发送时间
     */
    @ApiModelProperty(value = "定时发送时间")
    private LocalDateTime sendTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    public MessageCreate() {
    }

    public MessageCreate(Long messageTemplateId, String content, Integer businessType, Long businessId,
                         Integer level, Long sendUserId, Long receiveUserId, LocalDateTime sendTime) {
        this.messageTemplateId = messageTemplateId;
        this.content = content;
        this.businessType = businessType;
        this.businessId = businessId;
        this.level = level;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.sendTime = sendTime;
    }

}

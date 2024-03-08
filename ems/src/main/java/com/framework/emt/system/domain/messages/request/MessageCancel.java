package com.framework.emt.system.domain.messages.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(value = "挂起消息参数", description = "挂起消息参数")
public class MessageCancel implements Serializable {

    @NotNull(message = "业务类型不能为空！")
    @ApiModelProperty(value = "业务类型 1.异常提报 2.异常响应 3.异常处理 4.异常协同 5.异常验收")
    private Integer businessType;

    @NotNull(message = "通知级别不能为空！")
    @ApiModelProperty(value = "通知级别 1.普通 2.超时预警 3.超时上报")
    private Integer level;

    @NotNull(message = "业务id不能为空！")
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "接收人id")
    private Long receiveUserId;

    @ApiModelProperty(value = "定时发送时间")
    private Date sendTime;

    public MessageCancel() {
    }

    public MessageCancel cancel(Integer businessType, Integer level, Long businessId, Long receiveUserId) {
        this.businessType = businessType;
        this.level = level;
        this.businessId = businessId;
        this.receiveUserId = receiveUserId;
        return this;
    }

    public MessageCancel(Integer businessType, Integer level, Long businessId) {
        this.businessType = businessType;
        this.level = level;
        this.businessId = businessId;
    }
}

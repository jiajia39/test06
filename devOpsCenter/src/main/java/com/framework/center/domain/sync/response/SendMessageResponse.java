package com.framework.center.domain.sync.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "发送数据响应参数", description = "发送数据响应参数")
public class SendMessageResponse<T> {
    @ApiModelProperty(value = "响应消息")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;
    @ApiModelProperty(value = "响应代码")
    private Integer resultCode;
    @ApiModelProperty(value = "状态 是否成功")
    private Boolean state;
    @ApiModelProperty(value = "事务id")
    private String transactionId;
}

package com.framework.emt.system.domain.task.response.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常响应各个状态的数量 响应体
 *
 * @author gaojai
 * @since 2023-08-18
 */
@Getter
@Setter
public class ResponseStatusNumResponse implements Serializable {

    @ApiModelProperty(value = "待响应数量")
    private Long preResponseCount;

    @ApiModelProperty(value = "响应中数量")
    private Long respondingCount;

    @ApiModelProperty(value = "已超时数量")
    private Long isTimeOut;

    @ApiModelProperty(value = "已响应数量")
    private Long respondedCount;

    @ApiModelProperty(value = "所有数量")
    private Long allCount;

}

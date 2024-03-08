package com.framework.emt.system.domain.task.handing.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常处理各个状态的数量 处理体
 *
 * @author gaojai
 * @since 2023-08-18
 */
@Getter
@Setter
public class HandingStatusNumResponse implements Serializable {

    @ApiModelProperty(value = "待处理数量")
    private Long preHandingCount;

    @ApiModelProperty(value = "处理中数量")
    private Long handingCount;

    @ApiModelProperty(value = "已超时数量")
    private Long isTimeOut;

    @ApiModelProperty(value = "已完成数量")
    private Long finishCount;

    @ApiModelProperty(value = "所有数量")
    private Long allCount;

}

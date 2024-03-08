package com.framework.emt.system.domain.task.cooperation.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常协同各个状态的数量 响应体
 *
 * @author ds_C
 * @since 2023-08-16
 */
@Getter
@Setter
public class CooperationStatusNumResponse implements Serializable {

    @ApiModelProperty(value = "协同总数量")
    private Long totalCount;

    @ApiModelProperty(value = "待协同数量")
    private Long cooperationWaitCount;

    @ApiModelProperty(value = "已撤销总数")
    private Long revokedCount;

    @ApiModelProperty(value = "协同中总数")
    private Long cooperationIngCount;

    @ApiModelProperty(value = "已完成总数")
    private Long completedCount;

    @ApiModelProperty(value = "已超时总数")
    private Long timedOutCount;

    @ApiModelProperty(value = "所有数量")
    private Long allCount;
}

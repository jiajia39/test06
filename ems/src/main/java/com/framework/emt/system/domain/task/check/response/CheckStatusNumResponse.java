package com.framework.emt.system.domain.task.check.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常验收各个状态的数量 处理体
 *
 * @author gaojai
 * @since 2023-08-18
 */
@Getter
@Setter
public class CheckStatusNumResponse implements Serializable {

    @ApiModelProperty(value = "待验收数量")
    private Long checkCount;

    @ApiModelProperty(value = "已验收数量")
    private Long checkPassedCount;

    @ApiModelProperty(value = "已驳回数量")
    private Long checkRejectCount;

    @ApiModelProperty(value = "所有数量")
    private Long allCount;

}

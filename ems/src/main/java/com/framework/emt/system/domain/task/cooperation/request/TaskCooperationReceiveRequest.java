package com.framework.emt.system.domain.task.cooperation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常协同 接收参数
 *
 * @author ds_C
 * @since 2023-08-13
 */
@Getter
@Setter
public class TaskCooperationReceiveRequest implements Serializable {

    @NotNull(message = "异常协同id不能为空")
    @ApiModelProperty(value = "异常协同id")
    private Long id;

}

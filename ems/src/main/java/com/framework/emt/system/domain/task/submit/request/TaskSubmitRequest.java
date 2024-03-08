package com.framework.emt.system.domain.task.submit.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 立即提报 request
 *
 * @author ds_C
 * @since 2023-08-11
 */
@Getter
@Setter
public class TaskSubmitRequest implements Serializable {

    @NotNull(message = "异常提报id不能为空")
    @ApiModelProperty(value = "异常提报id", required = true)
    private Long id;

}

package com.framework.emt.system.domain.task.handing.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常处理协同 查询条件
 *
 * @author ds_C
 * @since 2023-08-20
 */
@Getter
@Setter
public class HandingCooperationQueryRequest extends Query implements Serializable {

    @NotNull(message = "异常处理id不能为空")
    @ApiModelProperty(value = "异常处理id", required = true)
    private Long handingId;

}

package com.framework.emt.system.domain.task.cooperation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常协同 转派参数
 *
 * @author ds_C
 * @since 2023-08-13
 */
@Getter
@Setter
public class TaskCooperationTransferRequest implements Serializable {

    @NotNull(message = "转派人id不能为空")
    @ApiModelProperty(value = "转派人id", required = true)
    private Long userId;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String otherRemark;

}

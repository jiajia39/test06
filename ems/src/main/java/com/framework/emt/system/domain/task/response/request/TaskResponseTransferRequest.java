package com.framework.emt.system.domain.task.response.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常任务转派 创建参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskResponseTransferRequest implements Serializable {

    @Length(max = 500, message = "转派备注长度限制{max}")
    @ApiModelProperty(value = "转派备注")
    private String otherRemark;

    @NotNull(message = "转派人id不能为空")
    @ApiModelProperty(value = "转派人id", required = true)
    private Long userId;
}

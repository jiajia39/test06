package com.framework.emt.system.domain.task.response.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 异常任务驳回 创建参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskResponseRejectRequest implements Serializable {

    @NotBlank(message = "驳回原因不能为空")
    @Length(max = 500, message = "驳回原因长度限制{max}")
    @ApiModelProperty(value = "驳回原因", required = true)
    private String rejectReason;

}

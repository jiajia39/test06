package com.framework.emt.system.domain.task.cooperation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常协同 创建参数
 *
 * @author ds_C
 * @since 2023-08-13
 */
@Getter
@Setter
public class TaskCooperationCreateRequest implements Serializable {

    @NotNull(message = "异常任务处理id不能为空")
    @ApiModelProperty(value = "异常任务处理id", required = true)
    private Long exceptionTaskHandingId;

    @NotBlank(message = "协同任务不能为空")
    @Length(max = 20, message = "协同任务长度限制{max}")
    @ApiModelProperty(value = "协同任务", required = true)
    private String title;

    @NotNull(message = "计划协同人id不能为空")
    @ApiModelProperty(value = "计划协同人id", required = true)
    private Long planUserId;

    @NotNull(message = "处理时限不能为空")
    @ApiModelProperty(value = "处理时限 单位:分钟", required = true)
    private Integer durationTime;

    @NotNull(message = "超时上报流程id不能为空")
    @ApiModelProperty(value = "超时上报流程id", required = true)
    private Long reportNoticeProcessId;

}

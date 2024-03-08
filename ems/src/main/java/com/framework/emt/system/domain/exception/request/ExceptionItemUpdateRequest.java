package com.framework.emt.system.domain.exception.request;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常项 更新参数
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionItemUpdateRequest implements Serializable {

    @NotBlank(message = "异常名称不能为空")
    @Length(max = 20, message = "异常名称长度限制{max}")
    @ApiModelProperty(value = "异常名称", required = true)
    private String title;

    @NotNull(message = "紧急程度不能为空")
    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急", required = true)
    private Integer priority;

    @NotNull(message = "严重程度不能为空")
    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命", required = true)
    private Integer severity;

    @NotNull(message = "响应时限不能为空")
    @ApiModelProperty(value = "响应时限 单位:分钟", required = true)
    private Integer responseDurationTime;

    @NotNull(message = "处理时限不能为空")
    @ApiModelProperty(value = "处理时限 单位:分钟", required = true)
    private Integer handingDurationTime;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String remark;

    @NotNull(message = "异常项状态不能为空")
    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "异常项状态 0:禁用 1:启用", required = true)
    private Integer enableFlag;

}

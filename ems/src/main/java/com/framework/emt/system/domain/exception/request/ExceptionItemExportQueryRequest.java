package com.framework.emt.system.domain.exception.request;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常项导出 查询条件
 *
 * @author ds_C
 * @since 2023-09-06
 */
@Getter
@Setter
public class ExceptionItemExportQueryRequest implements Serializable {

    @Length(max = 20, message = "异常项名称长度限制{max}")
    @ApiModelProperty(value = "异常项名称")
    private String title;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "异常项状态 0:禁用 1:启用")
    private Integer enableFlag;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命")
    private Integer severity;

    @Size(max = 10000, message = "主键id数目最大{max}条")
    @UniqueElementsValidator(message = "主键id不能重复")
    @ApiModelProperty(value = "主键id列表")
    private List<Long> ids;

}

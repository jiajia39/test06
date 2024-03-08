package com.framework.emt.system.domain.exception.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 异常项 查询条件
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionItemQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "异常项名称长度限制{max}")
    @ApiModelProperty(value = "异常项名称")
    private String title;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命")
    private Integer severity;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;
}

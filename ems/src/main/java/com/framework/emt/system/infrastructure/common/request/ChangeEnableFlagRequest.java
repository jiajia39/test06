package com.framework.emt.system.infrastructure.common.request;

import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 数据状态 更新条件
 *
 * @author ds_C
 * @since 2023-07-11
 */
@Getter
@Setter
public class ChangeEnableFlagRequest implements Serializable {

    @NotNull(message = "状态不能为空")
    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "状态 0:禁用 1:启用", required = true)
    private Integer enableFlag;

    @NotEmpty(message = "主键id列表不能为空")
    @Size(max = 1000, message = "主键id数目最大{max}条")
    @UniqueElementsValidator(message = "主键id不能重复")
    @ApiModelProperty(value = "主键id列表", required = true)
    private List<Long> idList;

}

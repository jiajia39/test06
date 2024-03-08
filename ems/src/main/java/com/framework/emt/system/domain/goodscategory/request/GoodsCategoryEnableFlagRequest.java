package com.framework.emt.system.domain.goodscategory.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常分类 启用禁用参数
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@ApiModel(value = "异常分类启用禁用参数")
@Data
public class GoodsCategoryEnableFlagRequest implements Serializable {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 状态 0:禁用 1:启用。保留字段
     */
    @ApiModelProperty(value = "状态 0:禁用 1:启用。保留字段", required = true)
    @Range(max = 1, min = 0, message = "启用禁用最大值是${max},最小值是${min}")
    @NotNull(message = "状态不能为空")
    private Integer enableFlag;


}

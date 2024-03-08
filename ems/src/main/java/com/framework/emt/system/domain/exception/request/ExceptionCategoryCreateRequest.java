package com.framework.emt.system.domain.exception.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常分类 创建参数
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionCategoryCreateRequest implements Serializable {

    @NotNull(message = "异常分类父ID不能为空")
    @ApiModelProperty(value = "异常分类父ID 顶级异常分类父ID为0", required = true)
    private Long parentId;

    @NotBlank(message = "分类名称不能为空")
    @Length(max = 20, message = "分类名称长度限制{max}")
    @ApiModelProperty(value = "分类名称", required = true)
    private String title;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

}

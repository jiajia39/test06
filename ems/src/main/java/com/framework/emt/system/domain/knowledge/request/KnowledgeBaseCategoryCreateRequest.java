package com.framework.emt.system.domain.knowledge.request;

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
 * 知识库分类 创建参数
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class KnowledgeBaseCategoryCreateRequest implements Serializable {

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

    @NotNull(message = "分类状态不能为空")
    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "分类状态 0:禁用 1:启用", required = true)
    private Integer enableFlag;

}

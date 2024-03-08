package com.framework.emt.system.domain.knowledge.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 知识库分类 查询条件
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class KnowledgeBaseCategoryQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "知识库分类名称长度限制{max}")
    @ApiModelProperty(value = "知识库分类名称")
    private String title;

    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "知识库分类状态 0:禁用 1:启用")
    private Integer enableFlag;

}

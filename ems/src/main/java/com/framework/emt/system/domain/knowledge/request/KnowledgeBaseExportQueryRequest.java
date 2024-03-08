package com.framework.emt.system.domain.knowledge.request;

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
 * 知识库导出 查询条件
 *
 * @author ds_C
 * @since 2023-07-15
 */
@Getter
@Setter
public class KnowledgeBaseExportQueryRequest implements Serializable {

    @Length(max = 20, message = "知识库标题长度限制{max}")
    @ApiModelProperty(value = "知识库标题")
    private String title;

    @ApiModelProperty(value = "知识库分类id")
    private Long knowledgeBaseCategoryId;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @Length(max = 20, message = "关键词长度限制{max}")
    @ApiModelProperty(value = "关键词")
    private String keyWord;

    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "异常项紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private Integer priority;

    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "异常项严重程度 0:轻微 1:一般 2:严重 3:致命")
    private Integer severity;

    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "知识库状态 0:禁用 1:启用")
    private Integer enableFlag;

    @Size(max = 10000, message = "主键id数目最大{max}条")
    @UniqueElementsValidator(message = "主键id不能重复")
    @ApiModelProperty(value = "主键id列表")
    private List<Long> idList;

}

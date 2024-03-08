package com.framework.emt.system.domain.knowledge.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 知识库标签 创建参数
 *
 * @author ds_C
 * @since 2023-08-03
 */
@Getter
@Setter
public class KnowledgeBaseTagCreateRequest implements Serializable {

    @NotBlank(message = "知识库关键词标签内容不能为空")
    @Length(max = 20, message = "知识库关键词标签内容长度限制{max}")
    @ApiModelProperty(value = "知识库关键词标签内容", required = true)
    private String tagContent;

}

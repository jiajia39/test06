package com.framework.emt.system.domain.knowledge.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 知识库标签 删除参数
 *
 * @author ds_C
 * @since 2023-08-04
 */
@Getter
@Setter
public class KnowledgeBaseTagDeleteRequest implements Serializable {

    @NotNull(message = "知识库id不能为空")
    @ApiModelProperty(value = "知识库id", required = true)
    private Long id;

    @NotNull(message = "知识库关键词标签id不能为空")
    @ApiModelProperty(value = "知识库关键词标签id", required = true)
    private Long tagId;

}

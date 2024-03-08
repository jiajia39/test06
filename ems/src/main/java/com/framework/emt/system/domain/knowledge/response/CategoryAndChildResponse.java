package com.framework.emt.system.domain.knowledge.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 知识库分类、知识库分类子集 响应体
 *
 * @author ds_C
 * @since 2023-07-27
 */
@Getter
@Setter
public class CategoryAndChildResponse implements Serializable {

    @ApiModelProperty(value = "知识库分类id")
    private Long id;

    @ApiModelProperty(value = "知识库分类名称")
    private String title;

    @ApiModelProperty(value = "知识库分类数量")
    private Long categoryCount;

    @ApiModelProperty(value = "知识库数量")
    private Long knowledgeBaseCount;

    @ApiModelProperty(value = "是否存在知识库")
    private Boolean hasKnowledgeBase;

}

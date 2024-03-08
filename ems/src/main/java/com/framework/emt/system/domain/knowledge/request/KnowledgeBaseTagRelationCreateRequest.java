package com.framework.emt.system.domain.knowledge.request;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 知识库标签关系 创建参数
 *
 * @author ds_C
 * @since 2023-08-04
 */
@Getter
@Setter
public class KnowledgeBaseTagRelationCreateRequest implements Serializable {

    @NotNull(message = "知识库id不能为空")
    @ApiModelProperty(value = "知识库id", required = true)
    private Long id;

    @ApiModelProperty(value = "知识库关键词标签id")
    private Long tagId;

    @Length(max = 20, message = "知识库关键词标签内容长度限制{max}")
    @ApiModelProperty(value = "知识库关键词标签内容")
    private String tagContent;

    public KnowledgeBaseTagRelationCreateRequest init() {
        if (ObjectUtil.isNotNull(tagId)) {
            tagContent = null;
        }
        if (StringUtils.isNotBlank(tagContent)) {
            tagId = null;
        }
        return this;
    }

}

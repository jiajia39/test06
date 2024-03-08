package com.framework.emt.system.domain.knowledge.response;

import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 知识库分类 响应体
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Getter
@Setter
public class KnowledgeBaseCategoryResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "知识库分类id")
    private Long id;

    @ApiModelProperty(value = "知识库分类名称")
    private String title;

    @ApiModelProperty(value = "知识库分类状态 0:禁用 1:启用")
    private EnableFlagEnum enableFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

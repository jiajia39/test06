package com.framework.emt.system.domain.exception.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常分类 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionCategoryResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long id;

    @ApiModelProperty(value = "异常分类父ID")
    private Long parentId;

    @ApiModelProperty(value = "异常分类父级ID路径")
    private String parentIdPath;

    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "所属上级")
    private List<String> parentTitleList;

    @ApiModelProperty(value = "是否存在子异常分类")
    private Boolean hasChildren;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

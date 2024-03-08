package com.framework.emt.system.domain.exception.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 异常分类树状图 响应体
 *
 * @author ds_C
 * @since 2023-08-07
 */
@Getter
@Setter
public class ExceptionCategoryTreeResponse implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long id;

    @ApiModelProperty(value = "父级ID路径")
    private Long parentId;

    @ApiModelProperty(value = "异常分类名称")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "异常分类下的子异常分类列表")
    private List<ExceptionCategoryTreeResponse> childList;

}

package com.framework.emt.system.domain.workspacelocation.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 作业单元 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class WorkspaceLocationResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "作业单元id")
    private Long id;

    @ApiModelProperty(value = "作业单元父ID")
    private Long parentId;

    private String parentTitle;
    @ApiModelProperty(value = "作业单元父级ID路径")
    private String parentIdPath;

    @ApiModelProperty(value = "作业单元名称")
    private String title;

    @ApiModelProperty(value = "空间坐标")
    private String spaceCoordinate;

    @ApiModelProperty(value = "所属上级")
    private List<String> parentTitleList;

    @ApiModelProperty(value = "是否存在子作业单元")
    private Boolean hasChildren;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

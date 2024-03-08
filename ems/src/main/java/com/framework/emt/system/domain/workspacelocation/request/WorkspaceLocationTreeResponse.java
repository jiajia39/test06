package com.framework.emt.system.domain.workspacelocation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 作业单元树状图 响应体
 *
 * @author ds_C
 * @since 2023-08-07
 */
@Getter
@Setter
public class WorkspaceLocationTreeResponse implements Serializable {

    @ApiModelProperty(value = "作业单元id")
    private Long id;

    @ApiModelProperty(value = "父级ID路径")
    private Long parentId;

    @ApiModelProperty(value = "作业单元名称")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "作业单元下的子作业单元列表")
    private List<WorkspaceLocationTreeResponse> children;

}

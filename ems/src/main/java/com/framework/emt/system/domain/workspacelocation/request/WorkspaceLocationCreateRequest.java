package com.framework.emt.system.domain.workspacelocation.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 作业单元 创建参数
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class WorkspaceLocationCreateRequest implements Serializable {

    @NotNull(message = "作业单元父ID不能为空")
    @ApiModelProperty(value = "作业单元父ID 顶级作业单元父ID为0", required = true)
    private Long parentId;

    @NotBlank(message = "作业单元名称不能为空")
    @Length(max = 20, message = "作业单元名称长度限制{max}")
    @ApiModelProperty(value = "作业单元名称", required = true)
    private String title;

    @Length(max = 20, message = "空间坐标长度限制{max}")
    @ApiModelProperty(value = "空间坐标")
    private String spaceCoordinate;

    @Length(max = 500, message = "备注长度限制{max}")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Range(min = 0, max = 99999, message = "排序最大{max}")
    @ApiModelProperty(value = "排序")
    private Integer sort;

}

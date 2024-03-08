package com.framework.emt.system.domain.workspacelocation.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 作业单元-导出校验 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class WorkspaceExportResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "作业单元id")
    private Long id;

    @ApiModelProperty(value = "作业单元名称")
    private String title;

    @ApiModelProperty(value = "父级id")
    private Long parentId;

    @ApiModelProperty(value = "父级作业单元名称")
    private String parentTitle;

}

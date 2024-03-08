package com.framework.emt.system.domain.workspacelocation.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 作业单元 查询条件
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class WorkspaceLocationQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "作业单元名称长度限制{max}")
    @ApiModelProperty(value = "作业单元名称")
    private String title;

    @ApiModelProperty(value = "作业单元父ID 顶级作业单元父ID为0")
    private Long parentId;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;
}

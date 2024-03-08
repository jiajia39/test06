package com.framework.emt.system.domain.task.response.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 异常任务响应 查询条件
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class SubmitExtendRequest implements Serializable {
    @NotBlank(message = "字段名不能为空")
    @ApiModelProperty(value = "字段名", required = true)
    private String prop;

    @NotBlank(message = "字段中文名不能为空")
    @ApiModelProperty(value = "字段中文名", required = true)
    private String label;

    @NotBlank(message = "字段值不能为空")
    @ApiModelProperty(value = "字段值", required = true)
    private String value;

    @ApiModelProperty(value = "字段注释 选择框-展示的信息")
    private String remark;

}

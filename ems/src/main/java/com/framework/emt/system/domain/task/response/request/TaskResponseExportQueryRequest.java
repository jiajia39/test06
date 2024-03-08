package com.framework.emt.system.domain.task.response.request;

import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常响应-导出 查询条件
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskResponseExportQueryRequest implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String categoryTitle;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "异常项名称")
    private String itemTitle;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "作业单元名称")
    private String workspaceLocationTitle;
    @ApiModelProperty(value = "提报部门id")
    private String deptId;

    @ApiModelProperty(value = "提报部门名称")
    private String deptName;

    @ApiModelProperty(value = "异常编号")
    private String code;

    @ApiModelProperty(value = "状态（0待响应，1响应中 2.已响应 3所有）")
    @Range(max = 3, min = 0, message = "状态最大值是3，最小值是0")
    private Integer status;

    @ApiModelProperty(value = "异常状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "是否超时 true:已超时 false:未超时")
    private Boolean expire;

    @Size(max = 10000, message = "异常响应id数目最大{max}条")
    @UniqueElementsValidator(message = "异常响应id不能重复")
    @ApiModelProperty(value = "异常响应id列表")
    private List<Long> ids;

    @ApiModelProperty(value = "需要展示的附加字段 1提报2.响应3处理4协同5验收6挂起")
    private List<Integer> isShowExtendField;

}

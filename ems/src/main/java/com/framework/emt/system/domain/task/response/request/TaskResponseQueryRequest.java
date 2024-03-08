package com.framework.emt.system.domain.task.response.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * 异常任务响应 查询条件
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskResponseQueryRequest extends Query implements Serializable {

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

    @ApiModelProperty(value = "状态（0待响应，1响应中 2.已响应 3所有 4已超时）")
    @Range(max = 4, min = 0, message = "状态最大值是4，最小值是0")
    private Integer status;

    @ApiModelProperty(value = "异常状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "是否超时 true:已超时 false:未超时")
    private Boolean expire;
    @ApiModelProperty(value = "异常状态列表")
    private List<Integer> taskStatusList;

    @ApiModelProperty(value = "异常项，异常编号模糊搜索")
    private String content;

}

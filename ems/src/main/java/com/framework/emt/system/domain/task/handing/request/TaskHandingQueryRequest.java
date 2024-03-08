package com.framework.emt.system.domain.task.handing.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常处理 查询条件
 *
 * @author ds_C
 * @since 2023-08-09
 */
@Getter
@Setter
public class TaskHandingQueryRequest extends Query implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "提报部门id")
    private String deptId;

    @ApiModelProperty(value = "异常编号")
    private String code;

    @ApiModelProperty(value = "异常状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "异常原因项")
    private String reasonItems;

    @ApiModelProperty(value = "挂起次数")
    private Integer suspendNum;

    @ApiModelProperty(value = "挂起次数是否正序  true:正序 false:倒叙")
    private Boolean suspendNumIsAsc;

    @ApiModelProperty(value = "挂起时长是否正序 true:正序 false:倒叙")
    private Boolean suspendSecondIsAsc;
    @ApiModelProperty(value = "是否超时 true:已超时 false:未超时")
    private Boolean isTimeOut;

    @ApiModelProperty(value = "状态 0:待处理 1:已挂起 2:已完成 3:处理中 4：所有 5已超时")
    private Integer status;

    @ApiModelProperty(value = "异常状态列表")
    private List<Integer> taskStatusList;

    @ApiModelProperty(value = "异常项，异常编号模糊搜索")
    private String content;
}
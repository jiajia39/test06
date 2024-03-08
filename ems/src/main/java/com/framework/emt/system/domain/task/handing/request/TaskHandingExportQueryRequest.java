package com.framework.emt.system.domain.task.handing.request;

import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 异常处理-导出 查询条件
 *
 * @author ds_C
 * @since 2023-08-30
 */
@Getter
@Setter
public class TaskHandingExportQueryRequest implements Serializable {

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

    @ApiModelProperty(value = "是否超时 true:已超时 false:未超时")
    private Boolean isTimeOut;

    @ApiModelProperty(value = "状态 0:待处理 1:已挂起 2:已完成 3:处理中 4：所有")
    private Integer status;

    @ApiModelProperty(value = "异常状态列表")
    private List<Integer> taskStatusList;

    @ApiModelProperty(value = "异常项，异常编号模糊搜索")
    private String content;

    @Size(max = 10000, message = "异常处理id数目最大{max}条")
    @UniqueElementsValidator(message = "异常处理id不能重复")
    @ApiModelProperty(value = "异常处理id列表")
    private List<Long> ids;

    @ApiModelProperty(value = "需要展示的附加字段 1提报2.响应3处理4协同5验收6挂起")
    private List<Integer> isShowExtendField;

}

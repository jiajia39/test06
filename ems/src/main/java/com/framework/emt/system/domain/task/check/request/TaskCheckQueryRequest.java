package com.framework.emt.system.domain.task.check.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * 异常任务验收 查询条件
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskCheckQueryRequest extends Query implements Serializable {

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

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "验收状态")
    @Range(max = 3, min = 1, message = "验证状态的值最小为1，最大为3")
    @EnumValidator(enumClazz = CheckStatus.class, message = "验收状态")
    private Integer checkStatus;

    @ApiModelProperty(value = "是否过期")
    private Boolean isTimeOut;

    @ApiModelProperty(value = "任务状态 全部：0")
    private Integer taskStatus;

    @ApiModelProperty(value = "异常状态列表")
    private List<Integer> taskStatusList;

    @ApiModelProperty(value = "异常项，异常编号模糊搜索")
    private String content;

}

package com.framework.emt.system.domain.task.check.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 异常任务验收 响应体
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskCheckListResponse extends BaseUserResponse implements Serializable {
    @ApiModelProperty(value = "验收id")
    private Long id;
    @ApiModelProperty(value = "异常编号")
    private String code;

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
    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;

    @ApiModelProperty(value = "提交处理人id")
    private Long userId;

    @ApiModelProperty(value = "提交处理人名称")
    private String userName;

    @ApiModelProperty(value = "验收状态")
    private CheckStatus checkStatus;
    @ApiModelProperty(value = "是否过期")
    private Boolean expiredOrNot;

    @ApiModelProperty(value = "验收时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "任务id")
    private Long taskId;
}

package com.framework.emt.system.domain.task.check.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.task.cooperation.response.TaskCooperationResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 异常任务验收 响应体
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskCheckDetailResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常编号")
    private String code;

    @ApiModelProperty(value = "异常状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "提报部门id")
    private String deptId;

    @ApiModelProperty(value = "提报部门名称")
    private String deptName;

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

    @ApiModelProperty(value = "紧急程度")
    private Integer priority;

    @ApiModelProperty(value = "严重程度")
    private Integer severity;
    @ApiModelProperty(value = "异常发生时间")
    private LocalDateTime faultTime;
    @ApiModelProperty(value = "异常描述")
    private LocalDateTime problem_desc;
    @ApiModelProperty(value = "提报附件")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "提报附加内容")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Object> submitExtendDatas;

    @ApiModelProperty(value = "提交处理人id")
    private Long submitHandingUserId;

    @ApiModelProperty(value = "提交处理人名称")
    private String submitHandingUserName;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime handingSubmitTime;

    @ApiModelProperty(value = "原因分析")
    private String submitReasonAnalysis;

    @ApiModelProperty(value = "解决方案")
    private String submitSolution;

    @ApiModelProperty(value = "处理结果")
    private String submitResult;

    @ApiModelProperty(value = "处理附件")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> handingFiles;
    @ApiModelProperty(value = "处理附加内容")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> handingExtendDatas;

    @ApiModelProperty(value = "协同列表")
    private List<TaskCooperationResponse> taskCooperationList;


    @ApiModelProperty(value = "验收时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "验收状态")
    private CheckStatus checkStatus;
    @ApiModelProperty(value = "是否过期")
    private Boolean expiredOrNot;
    @ApiModelProperty(value = "验收驳回原因")
    private String rejectReason;
    @ApiModelProperty(value = "验收验收附件")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> checkFiles;
    @ApiModelProperty(value = "验收附加内容")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> checkExtendDatas;
    @ApiModelProperty(value = "任务id")
    private Long taskId;
}

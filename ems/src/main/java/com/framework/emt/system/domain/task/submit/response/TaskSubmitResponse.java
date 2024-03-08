package com.framework.emt.system.domain.task.submit.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.common.response.UserInfoResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 异常提报 响应体
 *
 * @author ds_C
 * @since 2023-08-09
 */
@Getter
@Setter
public class TaskSubmitResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常提报id")
    private Long id;

    @ApiModelProperty(value = "异常编号")
    private String code;

    @ApiModelProperty(value = "异常状态")
    private ExceptionStatus taskStatus;

    @ApiModelProperty(value = "异常子状态")
    private ExceptionSubStatus taskSubStatus;

    @ApiModelProperty(value = "提报时间")
    private Date submitTime;

    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty(value = "异常流程名称")
    private String exceptionProcessTitle;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String exceptionCategoryTitle;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "异常项名称")
    private String exceptionItemTitle;

    @ApiModelProperty(value = "紧急程度")
    private PriorityEnum priority;

    @ApiModelProperty(value = "严重程度")
    private SeverityEnum severity;

    @ApiModelProperty(value = "响应时限")
    private Integer responseDurationTime;

    @ApiModelProperty(value = "处理时限")
    private Integer handingDurationTime;

    @ApiModelProperty(value = "响应超时上报流程id")
    private Long responseReportProcessId;

    @ApiModelProperty(value = "响应超时上报流程名称")
    private String responseReportProcessName;

    @ApiModelProperty(value = "处理超时上报流程id")
    private Long handingReportProcessId;

    @ApiModelProperty(value = "处理超时上报流程名称")
    private String handingReportProcessName;

    @ApiModelProperty(value = "提报部门id")
    private Long deptId;

    @ApiModelProperty(value = "提报部门名称")
    private String deptName;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "作业单元名称")
    private String workspaceLocationName;

    @ApiModelProperty(value = "异常发生时间")
    private Date faultTime;

    @ApiModelProperty(value = "异常描述")
    private String problemDesc;

    @ApiModelProperty(value = "附件")
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "验收成功通知人id列表")
    private List<Long> noticeUserIds;

    @ApiModelProperty(value = "验收成功通知人信息列表")
    private List<UserInfoResponse> noticeUserInfoList;

    @ApiModelProperty(value = "响应人id")
    private Long responseUserId;

    @ApiModelProperty(value = "响应人姓名")
    private String responseUserName;

    @ApiModelProperty(value = "提报人id")
    private Long submitUserId;

    @ApiModelProperty(value = "提报人姓名")
    private String submitUserName;

    @ApiModelProperty(value = "附加内容")
    private List<Object> submitExtendDatas;

    @ApiModelProperty(value = "驳回节点")
    private TaskRejectNode rejectNode;

    @ApiModelProperty(value = "驳回人id")
    private Long rejectUserId;

    @ApiModelProperty(value = "驳回人姓名")
    private String rejectUserName;

    @ApiModelProperty(value = "驳回时间")
    private Date rejectTime;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @ApiModelProperty(value = "异常任务提报版本号")
    private Integer taskSubmitVersion;

    @ApiModelProperty(value = "异常提报提报版本号")
    private Integer submitSubmitVersion;

}

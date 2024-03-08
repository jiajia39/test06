package com.framework.emt.system.domain.task.cooperation.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.Choice;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationSubStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 异常任务协同 响应体
 *
 * @author gaojia
 * @since 2023-08-09
 */
@Getter
@Setter
public class TaskCooperationResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "协同id")
    private Long id;

    @ApiModelProperty(value = "协同编号")
    private String cooperationCode;

    @ApiModelProperty(value = "异常任务处理id")
    private Long exceptionTaskHandingId;

    @ApiModelProperty(value = "协同状态")
    private CooperationStatus cooperationStatus;

    @ApiModelProperty(value = "协同子状态")
    private CooperationSubStatus cooperationSubStatus;

    @ApiModelProperty(value = "协同任务")
    private String title;

    @ApiModelProperty(value = "是否超时")
    private Choice isTimeOut;

    @ApiModelProperty(value = "处理时限 单位:分钟")
    private Integer durationTime;

    @ApiModelProperty(value = "异常编号")
    private String exceptionCode;

    @ApiModelProperty(value = "提报部门id")
    private Long deptId;

    @ApiModelProperty(value = "提报部门名称")
    private String deptName;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "异常项名称")
    private String exceptionItemName;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "作业单元名称")
    private String workspaceLocationName;

    @ApiModelProperty(value = "紧急程度")
    private PriorityEnum priority;

    @ApiModelProperty(value = "严重程度")
    private SeverityEnum severity;

    @ApiModelProperty(value = "异常发生时间")
    private Date faultTime;

    @ApiModelProperty(value = "异常描述")
    private String problemDesc;

    @ApiModelProperty(value = "提报附件")
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "提报附加内容")
    private List<Object> submitExtendDatas;

    @ApiModelProperty(value = "转派人id")
    private Long transferUserId;

    @ApiModelProperty(value = "转派人姓名")
    private String transferUserName;

    @ApiModelProperty(value = "转派时间")
    private Date otherTime;

    @ApiModelProperty(value = "转派信息")
    private String otherRemark;

    @ApiModelProperty(value = "处理人id")
    private Long handingUserId;

    @ApiModelProperty(value = "处理人姓名")
    private String handingUserName;

    @ApiModelProperty(value = "接受时间")
    private Date acceptTime;

    @ApiModelProperty(value = "处理最后期限")
    private Date finishDeadline;

    @ApiModelProperty(value = "完成时间")
    private Date submitTime;

    @ApiModelProperty(value = "原因分析")
    private String submitReasonAnalysis;

    @ApiModelProperty(value = "解决方案")
    private String submitSolution;

    @ApiModelProperty(value = "处理结果")
    private String submitResult;

    @ApiModelProperty(value = "协同附件")
    private List<FileRequest> cooperationSubmitFiles;

    @ApiModelProperty(value = "协同附加内容")
    private List<Object> cooperationSubmitExtendDatas;

    @ApiModelProperty(value = "协同人id")
    private Long userId;

    @ApiModelProperty(value = "协同人名称")
    private String userName;

}

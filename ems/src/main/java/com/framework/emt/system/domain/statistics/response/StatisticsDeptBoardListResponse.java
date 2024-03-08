package com.framework.emt.system.domain.statistics.response;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计部门看板汇总列表 查询结果
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsDeptBoardListResponse {
    private Long id;

    private Long exceptionProcessId;

    @ApiModelProperty("计划响应验收人")
    private List<Long> planResponseUserIds;
    @ApiModelProperty("计划响应验收人名称")
    private String planResponseUserName;
    @ApiModelProperty("计划处理人")
    private List<Long> planHandingUserIds;
    @ApiModelProperty("计划响应验收人名称")
    private String planHandingUserName;
    @ApiModelProperty("计划验收人")
    private List<Long> planCheckUserIds;
    @ApiModelProperty("计划响应验收人名称")
    private String planCheckUserName;
    @ApiModelProperty("响应提交时间")
    private LocalDateTime responseSubmitTime;

    @ApiModelProperty("响应过期时间")
    private LocalDateTime responseDeadline;

    @ApiModelProperty("处理提交时间")
    private LocalDateTime handingSubmitTime;

    @ApiModelProperty("处理过期时间")
    private LocalDateTime handingDeadline;

    @ApiModelProperty("异常项id")
    private String exceptionItemId;
    @ApiModelProperty("异常项名称")
    private String exceptionItemName;

    @ApiModelProperty("异常分类id")
    private String exceptionCategoryId;
    @ApiModelProperty("异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty("节点负责人")
    private Long userId;

    @ApiModelProperty("节点负责人姓名")
    private String userName;
    @ApiModelProperty("作业单元id")
    private Long workspaceLocationId;
    @ApiModelProperty("作业单元名称")
    private String workspaceLocationName;

    @ApiModelProperty("作业单元及父类名称")
    private String workspaceLocationAllName;
    //
//    @ApiModelProperty("响应节点剩余时间,单位：秒")
//    private Long responseRemainder;
//
    @ApiModelProperty("响应超时时长,单位：秒")
    private Long responseTimeoutSec;

    @ApiModelProperty("处理超时时长,单位：秒")
    private Long handingTimeoutSec;
    @ApiModelProperty("响应节点剩余时间,单位：秒")
    private Long responseRemainder;

    @ApiModelProperty("处理节点剩余时间,单位：秒")
    private Long handingRemainder;

//    @ApiModelProperty("超时时长,单位：秒")
//    private Long timeoutSec;

    @ApiModelProperty("紧急程度")
    private PriorityEnum priority;

    @ApiModelProperty("紧急程度名称")
    private String priorityName;

    @ApiModelProperty("严重程度")
    private SeverityEnum severity;

    @ApiModelProperty("严重程度名称")
    private String severityName;

    @ApiModelProperty("任务状态")
    private ExceptionStatus taskStatus;

    @ApiModelProperty("任务状态名称")
    private String taskStatusName;

    @ApiModelProperty("是否超时")
    private String isTimeOut;

    @ApiModelProperty("任务id")
    private Long taskId;

    public void init(List<StatisticsDeptBoardListResponse> list) {
        priorityName = priority.getName();
        severityName = severity.getName();
        if (StrUtil.isBlank(taskStatusName) && ObjectUtil.isNotNull(taskStatus)) {
            taskStatusName = taskStatus.getName();
        }

    }

    /**
     * 获取响应节点剩余时间 和超时时长
     *
     * @param now 当前时间
     */
    public void loadRemainderAndTimeout(LocalDateTime now) {
        if (ObjectUtil.isNotNull(taskStatus)) {
            taskStatusName = taskStatus.getName();
        }
        if (responseDeadline == null) {
            isTimeOut = null;
        } else {
            if (responseSubmitTime == null) {
                isTimeOut = now.isAfter(responseDeadline) ? "是" : "否";
                if (ObjectUtil.equal(isTimeOut, "是")) {
                    responseTimeoutSec = Duration.between(responseDeadline, now).getSeconds();
                } else {
                    responseRemainder = Duration.between(now, responseDeadline).getSeconds();
                }
            } else {
                isTimeOut = responseSubmitTime.isAfter(responseDeadline) ? "是" : "否";
                if (ObjectUtil.equal(isTimeOut, "是")) {
                    responseTimeoutSec = Duration.between(responseDeadline, responseSubmitTime).getSeconds();
                } else {
                    responseRemainder = Duration.between(responseSubmitTime, responseDeadline).getSeconds();
                }
            }
        }
        if (handingDeadline == null) {
            isTimeOut = null;
        } else {
            if (handingSubmitTime == null) {
                isTimeOut = now.isAfter(handingDeadline) ? "是" : "否";
                if (ObjectUtil.equal(isTimeOut, "是")) {
                    handingTimeoutSec = Duration.between(handingDeadline, now).getSeconds();
                }else {
                    handingRemainder = Duration.between(now, handingDeadline).getSeconds();
                }
            } else {
                isTimeOut = handingSubmitTime.isAfter(handingDeadline) ? "是" : "否";
                if (ObjectUtil.equal(isTimeOut, "是")) {
                    handingTimeoutSec = Duration.between(handingDeadline, handingSubmitTime).getSeconds();
                }else {
                    handingRemainder = Duration.between(handingSubmitTime, handingDeadline).getSeconds();
                }
            }
        }

    }

    public void getWorkspaceLocationName(Map<Long, WorkspaceLocationResponse> workspaceMap) {
        WorkspaceLocationResponse workspaceLocationResponse = workspaceMap.get(workspaceLocationId);
        if (workspaceLocationResponse == null) {
            workspaceLocationName = "";
            workspaceLocationAllName = "";
        } else {
            workspaceLocationName = workspaceLocationResponse.getTitle();
            if (StrUtil.isBlank(workspaceLocationResponse.getParentTitle())) {
                workspaceLocationAllName = workspaceLocationResponse.getTitle();
            } else {
                workspaceLocationAllName = workspaceLocationResponse.getParentTitle() + "," + workspaceLocationName;
            }
        }
    }

    public List<Long> initUserList() {
        List<Long> userIdList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(planResponseUserIds) && planResponseUserIds.size() > 0) {
            userIdList.addAll(planResponseUserIds);
        }
        if (ObjectUtil.isNotEmpty(planHandingUserIds) && planHandingUserIds.size() > 0) {
            userIdList.addAll(planHandingUserIds);
        }
        if (ObjectUtil.isNotEmpty(planCheckUserIds) && planCheckUserIds.size() > 0) {
            userIdList.addAll(planCheckUserIds);
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap) {
        this.planResponseUserName = planResponseUserIds.stream().map(userId -> DataUtils.getUserName(userMap, userId)).collect(Collectors.joining(StrPool.COMMA));
        this.planHandingUserName = planHandingUserIds.stream().map(userId -> DataUtils.getUserName(userMap, userId)).collect(Collectors.joining(StrPool.COMMA));
        this.planCheckUserName = planCheckUserIds.
                stream().map(userId -> DataUtils.getUserName(userMap, userId)).collect(Collectors.joining(StrPool.COMMA));
    }

}



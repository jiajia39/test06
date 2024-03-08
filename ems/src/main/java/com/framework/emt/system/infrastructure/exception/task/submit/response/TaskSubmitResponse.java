package com.framework.emt.system.infrastructure.exception.task.submit.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.IdNameResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常提报返回数据
 *
 * @author jiaXue
 * date 2023/8/17
 */
@Getter
@Setter
public class TaskSubmitResponse {

    @ApiModelProperty("提报部门id")
    private Long deptId;

    @ApiModelProperty("提报部门名称")
    private String deptName;

    @ApiModelProperty("异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty("异常流程名称")
    private String exceptionProcessTitle;

    @ApiModelProperty("异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty("异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty("异常项id")
    private Long exceptionItemId;

    @ApiModelProperty("异常项名称")
    private String exceptionItemName;

    @ApiModelProperty("紧急程度")
    private PriorityEnum priority;

    @ApiModelProperty("紧急程度名称")
    private String priorityName;

    @ApiModelProperty("严重程度")
    private SeverityEnum severity;

    @ApiModelProperty("严重程度名称")
    private String severityName;

    @ApiModelProperty("响应时限 单位:分钟")
    private Integer responseDurationTime;

    @ApiModelProperty("处理时限 单位:分钟")
    private Integer handingDurationTime;

    @ApiModelProperty(value = "响应超时上报流程id")
    private Long responseReportProcessId;

    @ApiModelProperty(value = "响应超时上报流程名称")
    private String responseReportProcessName;

    @ApiModelProperty(value = "处理超时上报流程id")
    private Long handingReportProcessId;

    @ApiModelProperty(value = "处理超时上报流程名称")
    private String handingReportProcessName;

    @ApiModelProperty("作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty("作业单元名称")
    private String workspaceLocationName;

    @ApiModelProperty("作业单元及父类名称")
    private String workspaceLocationAllName;
    @ApiModelProperty("故障时间")
    private LocalDateTime faultTime;

    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("提报响应人id")
    private Long submitResponseUserId;

    @ApiModelProperty("提报响应人名称")
    private String submitResponseUserName;

    @ApiModelProperty("提报附件列表")
    private List<FileRequest> submitFiles;

    @ApiModelProperty("验收成功通知人id")
    private List<Long> noticeUserIds;

    @ApiModelProperty("验收成功通知人")
    private List<IdNameResponse> noticeUserList;

    @ApiModelProperty("提报时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("提交附加数据")
    private List<FormFieldResponse> submitExtendDatas;

    @ApiModelProperty("提报人id")
    private Long submitUserId;

    @ApiModelProperty("提报人名称")
    private String submitUserName;

    @ApiModelProperty("提报驳回节点")
    private TaskRejectNode rejectNode;

    @ApiModelProperty("提报驳回节点名称")
    private String rejectNodeName;

    @ApiModelProperty("驳回来源id")
    private Long rejectSourceId;

    @ApiModelProperty("驳回时间")
    private LocalDateTime rejectTime;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("驳回人")
    private Long rejectUserId;

    @ApiModelProperty("驳回人名称")
    private String rejectUserName;

    public void init() {
        priorityName = priority.getName();
        severityName = severity.getName();
        rejectNodeName = rejectNode.getName();
        noticeUserList = new ArrayList<>();
    }

    public List<Long> initUserIdList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(submitResponseUserId)) {
            userIdList.add(submitResponseUserId);
        }
        if (CollectionUtil.isNotEmpty(noticeUserIds)) {
            userIdList.addAll(noticeUserIds);
        }
        if (DataUtils.isId(submitUserId)) {
            userIdList.add(submitUserId);
        }
        if (DataUtils.isId(rejectUserId)) {
            userIdList.add(rejectUserId);
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap,
                       Map<Long, Dept> deptMap,
                       Map<Long, ExceptionCategory> categoryMap,
                       Map<Long, ExceptionItem> itemMap,
                       Map<Long, WorkspaceLocationResponse> workspaceMap) {
        deptName = deptMap.get(deptId) == null ? "" : deptMap.get(deptId).getDeptName();
        exceptionCategoryName = categoryMap.get(exceptionCategoryId) == null ? "" : categoryMap.get(exceptionCategoryId).getTitle();
        exceptionItemName = itemMap.get(exceptionItemId) == null ? "" : itemMap.get(exceptionItemId).getTitle();

        WorkspaceLocationResponse workspaceLocationResponse = workspaceMap.get(workspaceLocationId);
        if (workspaceLocationResponse == null) {
            workspaceLocationName = "";
            workspaceLocationAllName = "";
        }else {
            workspaceLocationName = workspaceLocationResponse.getTitle();
            if (StrUtil.isBlank(workspaceLocationResponse.getParentTitle())) {
                workspaceLocationAllName = workspaceLocationResponse.getTitle();
            } else {
                workspaceLocationAllName = workspaceLocationResponse.getParentTitle() + "," + workspaceLocationName;
            }
        }
        submitResponseUserName = DataUtils.getUserName(userMap, submitResponseUserId);
        submitUserName = DataUtils.getUserName(userMap, submitUserId);
        rejectUserName = DataUtils.getUserName(userMap, rejectUserId);

        if (CollectionUtil.isNotEmpty(noticeUserIds)) {
            noticeUserList = noticeUserIds
                    .stream()
                    .filter(userMap::containsKey)
                    .map(userId -> new IdNameResponse(userId, DataUtils.getUserName(userMap, userId)))
                    .collect(Collectors.toList());
        }
    }

}

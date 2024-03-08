package com.framework.emt.system.infrastructure.exception.task.response.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
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
 * 异常响应返回数据
 *
 * @author jiaXue
 * date 2023/8/17
 */
@Getter
@Setter
public class TaskResResponse {

    @ApiModelProperty("计划响应人id列表")
    private List<Long> planUserIdList;

    @ApiModelProperty("计划响应人名称列表")
    private String planUserNameList;

    @ApiModelProperty("响应/转派人id")
    private Long userId;

    @ApiModelProperty("响应/转派人名称")
    private String userName;

    @ApiModelProperty("转派备注")
    private String otherRemark;

    @ApiModelProperty("接受人id")
    private Long acceptUserId;

    @ApiModelProperty("接受人名称")
    private String acceptUserName;

    @ApiModelProperty("接受时间")
    private LocalDateTime acceptTime;

    @ApiModelProperty("提交处理人id")
    private Long submitHandingUserId;

    @ApiModelProperty("提交处理人名称")
    private String submitHandingUserName;

    @ApiModelProperty("提交附加数据")
    private List<FormFieldResponse> submitExtendDatas;

    @ApiModelProperty("转派时间")
    private LocalDateTime otherTime;

    @ApiModelProperty("提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("驳回次数")
    private Integer rejectNum;

    @ApiModelProperty("驳回节点")
    private TaskRejectNode rejectNode;

    @ApiModelProperty("驳回节点名称")
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

    @ApiModelProperty("是否超时")
    private Integer expire;

    public void init(LocalDateTime now, LocalDateTime responseDeadline) {
        if (ObjectUtil.isNotNull(rejectNode)) {
            rejectNodeName = rejectNode.getName();
        }
        if (responseDeadline == null) {
            expire = -1;
            return;
        }
        if (submitTime == null) {
            expire = now.isAfter(responseDeadline) ? 1 : 0;
        } else {
            expire = submitTime.isAfter(responseDeadline) ? 1 : 0;
        }
    }

    public List<Long> initUserIdList() {
        List<Long> userIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(planUserIdList)) {
            userIdList.addAll(planUserIdList);
        }else {
            planUserIdList = new ArrayList<>();
        }
        if (DataUtils.isId(userId)) {
            userIdList.add(userId);
        }
        if (DataUtils.isId(rejectUserId)) {
            userIdList.add(rejectUserId);
        }
        if (DataUtils.isId(submitHandingUserId)) {
            userIdList.add(submitHandingUserId);
        }
        if (DataUtils.isId(acceptUserId)) {
            userIdList.add(acceptUserId);
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap) {
        this.planUserNameList = planUserIdList.stream()
                .map(userId -> DataUtils.getUserName(userMap, userId))
                .collect(Collectors.joining(StrPool.COMMA));
        userName = DataUtils.getUserName(userMap, userId);
        rejectUserName = DataUtils.getUserName(userMap, rejectUserId);
        submitHandingUserName = DataUtils.getUserName(userMap, submitHandingUserId);
        acceptUserName = DataUtils.getUserName(userMap, acceptUserId);

    }


}

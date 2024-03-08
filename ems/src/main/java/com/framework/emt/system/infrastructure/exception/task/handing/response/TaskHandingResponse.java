package com.framework.emt.system.infrastructure.exception.task.handing.response;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.domain.tag.response.TagInfo;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.TaskRejectNode;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 异常处理返回数据
 *
 * @author jiaXue
 * date 2023/8/17
 */
@Getter
@Setter
@Slf4j
public class TaskHandingResponse {
    @ApiModelProperty("处理id")
    private Long handingId;

    @ApiModelProperty("计划处理人id列表")
    private List<Long> planUserIdList;

    @ApiModelProperty("计划处理人名称列表")
    private String planUserNameList;

    @ApiModelProperty("处理/转派人id")
    private Long userId;

    @ApiModelProperty("处理/转派人名称")
    private String userName;

    @ApiModelProperty("转派备注")
    private String otherRemark;

    @ApiModelProperty("转派时间")
    private LocalDateTime otherTime;

    @ApiModelProperty("接受人id")
    private Long acceptUserId;

    @ApiModelProperty("接受人名称")
    private String acceptUserName;

    @ApiModelProperty("接受时间")
    private LocalDateTime acceptTime;

    @ApiModelProperty("挂起时间")
    private LocalDateTime suspendTime;

    @ApiModelProperty("挂起原因")
    private String suspendReason;

    @ApiModelProperty("挂起次数")
    private Integer suspendNum;

    @ApiModelProperty("处理挂起总时长")
    private Long suspendTotalSecond;

    @ApiModelProperty("恢复方式 0:未恢复 1:自动 2:手动")
    private TaskResumeType resumeType;

    @ApiModelProperty("预计恢复时间")
    private LocalDateTime resumeTime;

    @ApiModelProperty("实际恢复时间")
    private LocalDateTime resumeRealTime;

    @ApiModelProperty("提交处理人id")
    private Long submitHandingUserId;

    @ApiModelProperty("提交处理人名称")
    private String submitHandingUserName;

    @ApiModelProperty("原因分析")
    private String reasonAnalysis;

    @ApiModelProperty("解决方案")
    private String solution;

    @ApiModelProperty("处理结果")
    private String result;

    @ApiModelProperty("异常原因项列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TagInfo> reasonItems;

    @ApiModelProperty("提交附加数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> extendDatas;

    @ApiModelProperty("提交附件列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> files;

    @ApiModelProperty("提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("驳回节点 0:未驳回 1:响应 2:处理 3:验收")
    private TaskRejectNode rejectNode;

    @ApiModelProperty("驳回次数")
    private Integer rejectNum;

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

    @ApiModelProperty("挂起附加字段")
    private List<FormFieldResponse> suspendExtendDatas;

    @ApiModelProperty("挂起附件列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> suspendFiles;
    @ApiModelProperty("是否超时")
    private Integer expire;

    @ApiModelProperty(value = "挂起时长")
    private Integer suspendSecond;


    public void init(LocalDateTime now, LocalDateTime handingDeadline) {
        if (ObjectUtil.isNotNull(rejectNode)) {
            rejectNodeName = rejectNode.getName();
        }
        if (handingDeadline == null) {
            expire = -1;
            return;
        }
        if (submitTime == null) {
            expire = now.isAfter(handingDeadline) ? 1 : 0;
        } else {
            expire = submitTime.isAfter(handingDeadline) ? 1 : 0;
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
        if (DataUtils.isId(acceptUserId)) {
            userIdList.add(acceptUserId);
        }
        if (DataUtils.isId(submitHandingUserId)) {
            userIdList.add(submitHandingUserId);
        }
        if (DataUtils.isId(rejectUserId)) {
            userIdList.add(rejectUserId);
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

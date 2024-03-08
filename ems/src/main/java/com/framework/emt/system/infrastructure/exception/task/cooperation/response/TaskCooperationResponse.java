package com.framework.emt.system.infrastructure.exception.task.cooperation.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationStatus;
import com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums.CooperationSubStatus;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 异常协同返回数据
 *
 * @author ds_C
 * @since 2023-08-19
 */
@Getter
@Setter
public class TaskCooperationResponse implements Serializable {

    @ApiModelProperty(value = "协同状态")
    private CooperationStatus cooperationStatus;

    @ApiModelProperty(value = "协同状态名称")
    private String cooperationStatusName;

    @ApiModelProperty(value = "协同子状态")
    private CooperationSubStatus cooperationSubStatus;

    @ApiModelProperty(value = "协同子状态名称")
    private String cooperationSubStatusName;

    @ApiModelProperty(value = "协同标题")
    private String title;

    @ApiModelProperty(value = "超时上报流程id")
    private Long reportNoticeProcessId;

    @ApiModelProperty(value = "超时上报流程名称")
    private String reportNoticeProcessName;

    @ApiModelProperty(value = "处理时限 单位:分钟")
    private Integer durationTime;

    @ApiModelProperty(value = "处理最后期限")
    private LocalDateTime finishDeadline;

    @ApiModelProperty(value = "计划协同人id")
    private Long planUserId;

    @ApiModelProperty(value = "计划协同人名称")
    private String planUserName;

    @ApiModelProperty(value = "协同/转派人id")
    private Long userId;

    @ApiModelProperty(value = "协同/转派人名称")
    private String userName;

    @ApiModelProperty(value = "转派人id")
    private Long transferUserId;

    @ApiModelProperty(value = "转派人名称")
    private String transferUserName;

    @ApiModelProperty(value = "转派备注")
    private String otherRemark;

    @ApiModelProperty(value = "转派时间")
    private Date otherTime;

    @ApiModelProperty(value = "接受人id")
    private Long acceptUserId;

    @ApiModelProperty(value = "接受时间")
    private Date acceptTime;

    @ApiModelProperty(value = "原因分析")
    private String submitReasonAnalysis;

    @ApiModelProperty(value = "解决方案")
    private String submitSolution;

    @ApiModelProperty(value = "处理结果")
    private String submitResult;

    @ApiModelProperty(value = "提交附件列表")
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "提交附加数据")
    private List<FormFieldResponse> submitExtendDatas;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("是否超时")
    private Integer expire;

    public void init(LocalDateTime now) {
        cooperationStatusName = cooperationStatus.getName();
        cooperationSubStatusName = cooperationSubStatus.getName();
        if (finishDeadline == null) {
            expire = -1;
            return;
        }
        if (submitTime == null) {
            expire = now.isAfter(finishDeadline) ? 1 : 0;
        } else {
            expire = submitTime.isAfter(finishDeadline) ? 1 : 0;
        }
    }

    public List<Long> initUserIdList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(planUserId)) {
            userIdList.add(planUserId);
        }
        if (DataUtils.isId(userId)) {
            userIdList.add(userId);
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap) {
        planUserName = DataUtils.getUserName(userMap, planUserId);
        userName = DataUtils.getUserName(userMap, userId);
        if (ObjectUtil.equals(planUserId, userId)) {
            transferUserId = NumberUtils.LONG_ZERO;
            transferUserName = StrUtil.EMPTY;
        } else {
            transferUserId = userId;
            transferUserName = userName;
        }
    }

}

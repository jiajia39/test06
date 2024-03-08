package com.framework.emt.system.infrastructure.exception.task.task.response;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.admin.system.entity.Dept;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.exception.ExceptionCategory;
import com.framework.emt.system.domain.exception.ExceptionItem;
import com.framework.emt.system.domain.workspacelocation.WorkspaceLocation;
import com.framework.emt.system.domain.workspacelocation.response.WorkspaceLocationResponse;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import com.framework.emt.system.infrastructure.exception.task.check.response.TaskCheckResponse;
import com.framework.emt.system.infrastructure.exception.task.cooperation.response.TaskCooperationResponse;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionStatus;
import com.framework.emt.system.infrastructure.exception.task.task.constant.enums.ExceptionSubStatus;
import com.framework.emt.system.infrastructure.exception.task.handing.response.TaskHandingResponse;
import com.framework.emt.system.infrastructure.exception.task.response.response.TaskResResponse;
import com.framework.emt.system.infrastructure.exception.task.submit.response.TaskSubmitResponse;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 异常基础返回数据
 *
 * @author jiaXue
 * date 2023/8/17
 */

@Getter
@Setter
public class TaskResponse extends BaseUserResponse {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "异常id")
    private Long taskId;

    @ApiModelProperty(value = "异常状态")
    private ExceptionStatus taskStatus;

    @ApiModelProperty(value = "异常状态名称")
    private String taskStatusName;

    @ApiModelProperty(value = "异常子状态")
    private ExceptionSubStatus taskSubStatus;

    @ApiModelProperty(value = "异常子状态名称")
    private String taskSubStatusName;

    @ApiModelProperty(value = "提报版本号")
    private Integer submitVersion;

    @ApiModelProperty(value = "异常关闭时间")
    private LocalDateTime closeTime;

    @ApiModelProperty(value = "响应最后期限")
    private LocalDateTime responseDeadline;

    @ApiModelProperty(value = "处理最后期限")
    private LocalDateTime handingDeadline;

    @ApiModelProperty(value = "响应版本号")
    private Integer responseVersion;

    @ApiModelProperty(value = "响应转派次数")
    private Integer responseOtherCount;

    @ApiModelProperty(value = "响应总时长")
    private Integer responseTotalSecond;

    @ApiModelProperty(value = "响应驳回次数")
    private Integer responseRejectNum;

    @ApiModelProperty(value = "处理版本号")
    private Integer handingVersion;

    @ApiModelProperty(value = "处理转派次数")
    private Integer handingOtherCount;

    @ApiModelProperty(value = "处理挂起次数")
    private Integer handingSuspendNum;

    @ApiModelProperty(value = "处理挂起总时长")
    private long handingSuspendTotalSecond;

    @ApiModelProperty(value = "处理总时长")
    private Integer handingTotalSecond;

    @ApiModelProperty(value = "验收版本号")
    private Integer checkVersion;

    @ApiModelProperty(value = "验收总时长")
    private Integer checkTotalSecond;

    @ApiModelProperty(value = "处理驳回次数")
    private Integer handingRejectNum;

    @ApiModelProperty(value = "待验收数目")
    private Integer checkCount;

    @ApiModelProperty(value = "已验收数目")
    private Integer checkedCount;

    @ApiModelProperty(value = "验收结束时间")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "提报信息")
    private TaskSubmitResponse submit;

    @ApiModelProperty(value = "响应信息")
    private TaskResResponse response;

    @ApiModelProperty(value = "处理信息")
    private TaskHandingResponse handing;

    @ApiModelProperty(value = "协同信息")
    private TaskCooperationResponse cooperation;

    @ApiModelProperty(value = "验收信息")
    private TaskCheckResponse check;

    public void init(LocalDateTime now) {
        if (submit != null) {
            initStatus();
            submit.init();
        }
        if (response != null) {
            initStatus();
            response.init(now, responseDeadline);
        }
        if (handing != null) {
            handing.init(now, handingDeadline);
        }
        if (cooperation != null) {
            cooperation.init(now);
        }
        if (check != null) {
            check.init();
        }
    }

    private void initStatus() {
        taskStatusName = taskStatus.getName();
        taskSubStatusName = taskSubStatus.getName();
    }

    public List<Long> initUserList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(createUser)) {
            userIdList.add(createUser);
        }
        if (DataUtils.isId(updateUser)) {
            userIdList.add(updateUser);
        }
        if (submit != null) {
            userIdList.addAll(submit.initUserIdList());
        }
        if (response != null) {
            userIdList.addAll(response.initUserIdList());
        }
        if (handing != null) {
            userIdList.addAll(handing.initUserIdList());
        }
        if (cooperation != null) {
            userIdList.addAll(cooperation.initUserIdList());
        }
        if (check != null) {
            userIdList.addAll(check.initUserIdList());
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap,
                       Map<Long, Dept> deptMap,
                       Map<Long, ExceptionCategory> categoryMap,
                       Map<Long, ExceptionItem> itemMap,
                       Map<Long, WorkspaceLocationResponse> workspaceMap) {
        if (CollectionUtil.isEmpty(userMap)) {
            return;
        }
        createUserName = DataUtils.getUserName(userMap, createUser);
        updateUserName = DataUtils.getUserName(userMap, updateUser);
        if (submit != null) {
            submit.update(userMap,
                    deptMap,
                    categoryMap,
                    itemMap,
                    workspaceMap);
        }
        if (response != null) {
            response.update(userMap);
        }
        if (handing != null) {
            handing.update(userMap);
        }
        if (cooperation != null) {
            cooperation.update(userMap);
        }
        if (check != null) {
            check.update(userMap);
        }
    }

}

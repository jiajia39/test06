package com.framework.emt.system.infrastructure.exception.task.check.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.admin.system.entity.User;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import com.framework.emt.system.infrastructure.utils.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 异常验收返回数据
 *
 * @author gaojia
 * date 2023/8/20
 */
@Getter
@Setter
public class TaskCheckResponse {

    @ApiModelProperty("异常任务表id")
    private Long exceptionTaskId;

    @ApiModelProperty("验收人id")
    private Long userId;

    @ApiModelProperty("验收人名称")
    private String userName;

    @ApiModelProperty("验收状态")
    private CheckStatus checkStatus;

    @ApiModelProperty("验收子状态")
    private CheckSubStatus checkSubstatus;

    @ApiModelProperty("提交附加数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas;

    @ApiModelProperty("验收时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("拒绝原因")
    private String rejectReason;

    @ApiModelProperty("提交附件列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "是否过期")
    private Integer expiredOrNot;

    public void init() {
        if (checkSubstatus == null) {
            expiredOrNot = -1;
            return;
        }
        if (checkSubstatus.equals(CheckSubStatus.WAIT_CHECK_EXPIRED)) {
            expiredOrNot = 1;
        } else {
            expiredOrNot = 0;
        }
    }

    public List<Long> initUserIdList() {
        List<Long> userIdList = new ArrayList<>();
        if (DataUtils.isId(userId)) {
            userIdList.add(userId);
        }
        return userIdList;
    }

    public void update(Map<Long, User> userMap) {
        userName = DataUtils.getUserName(userMap, userId);
    }

}

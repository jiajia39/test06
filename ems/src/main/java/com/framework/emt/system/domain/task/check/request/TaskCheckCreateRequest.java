package com.framework.emt.system.domain.task.check.request;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.emt.system.domain.formfield.response.FormFieldResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckStatus;
import com.framework.emt.system.infrastructure.exception.task.check.constant.enums.CheckSubStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常任务验收 创建参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskCheckCreateRequest implements Serializable {
    @NotNull(message = "异常任务表id不能为空")
    @ApiModelProperty(value = "异常任务表id", required = true)
    private Long exceptionTaskId;

    @ApiModelProperty(value = "验收版本号")
    private Integer checkVersion;

    @NotNull(message = "验收人id不能为空")
    @ApiModelProperty(value = "验收人id", required = true)
    private List<Long> userIdList = new ArrayList<>();
    @ApiModelProperty(value = "验收人id")
    private Long userId;
    @NotNull(message = "验收状态不能为空")
    @EnumValidator(enumClazz = CheckStatus.class, message = "验收状态")
    @ApiModelProperty(value = "验收状态", required = true)
    private Integer checkStatus;


    @NotNull(message = "验收子状态不能为空")
    @EnumValidator(enumClazz = CheckSubStatus.class, message = "验收状态")
    @ApiModelProperty(value = "验收子状态", required = true)
    private Integer checkSubstatus;

    @ApiModelProperty(value = "提交附加数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FormFieldResponse> submitExtendDatas = new ArrayList<>();
    ;


    @ApiModelProperty(value = "验收时间")
    private LocalDateTime submitTime;


    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;


    @ApiModelProperty(value = "提交附件列表")
    private List<FileRequest> submitFiles;


    public void checkCreate(Long exceptionTaskId, Integer checkVersion, List<Long> userIdList, List<FormFieldResponse> submitExtendDatas) {
        this.exceptionTaskId = exceptionTaskId;
        this.checkVersion = checkVersion;
        this.userIdList = userIdList;
        this.checkSubstatus = CheckSubStatus.WAIT_CHECK.getCode();
        this.checkStatus = CheckStatus.WAIT_CHECK.getCode();
        this.submitExtendDatas = submitExtendDatas;
    }

}

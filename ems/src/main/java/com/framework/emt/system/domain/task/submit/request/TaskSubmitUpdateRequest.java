package com.framework.emt.system.domain.task.submit.request;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.common.validation.UniqueElementsValidator;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 异常提报 更新参数
 *
 * @author ds_C
 * @since 2023-08-08
 */
@Getter
@Setter
public class TaskSubmitUpdateRequest implements Serializable {

    @NotNull(message = "异常流程id不能为空")
    @ApiModelProperty(value = "异常流程id", required = true)
    private Long exceptionProcessId;

    @NotNull(message = "异常项id不能为空")
    @ApiModelProperty(value = "异常项id", required = true)
    private Long exceptionItemId;

    @NotNull(message = "紧急程度不能为空")
    @EnumValidator(enumClazz = PriorityEnum.class, message = "紧急程度类型错误 0:轻微 1:一般 2:紧急 3:特急")
    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急", required = true)
    private Integer priority;

    @NotNull(message = "严重程度不能为空")
    @EnumValidator(enumClazz = SeverityEnum.class, message = "严重程度类型错误 0:轻微 1:一般 2:严重 3:致命")
    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命", required = true)
    private Integer severity;

    @NotNull(message = "响应时限不能为空")
    @ApiModelProperty(value = "响应时限 单位:分钟")
    private Integer responseDurationTime;

    @NotNull(message = "处理时限不能为空")
    @ApiModelProperty(value = "处理时限 单位:分钟")
    private Integer handingDurationTime;

    @NotNull(message = "响应人id不能为空")
    @ApiModelProperty(value = "响应人id", required = true)
    private Long responseUserId;

    @NotNull(message = "提报部门id不能为空")
    @ApiModelProperty(value = "提报部门id", required = true)
    private Long deptId;

    @UniqueElementsValidator(message = "验收成功通知人id不能重复")
    @ApiModelProperty(value = "验收成功通知人id列表")
    private List<Long> noticeUserIds;

    @NotNull(message = "作业单元id不能为空")
    @ApiModelProperty(value = "作业单元id", required = true)
    private Long workspaceId;

    @NotBlank(message = "异常描述不能为空")
    @Length(max = 500, message = "异常描述长度限制{max}")
    @ApiModelProperty(value = "异常描述", required = true)
    private String problemDesc;

    @Past(message = "异常发生时间不能超过当前时间")
    @ApiModelProperty(value = "异常发生时间")
    private LocalDateTime faultTime;

    @ApiModelProperty(value = "附件")
    private List<FileRequest> submitFiles;

    @ApiModelProperty(value = "提报附加字段")
    private List<ExtendFieldsRequest> submitExtendDatas;

    public TaskSubmitUpdateRequest init() {
        if (submitFiles == null) {
            submitFiles = Collections.emptyList();
        }
        if (noticeUserIds == null) {
            noticeUserIds = Collections.emptyList();
        }
        if (submitExtendDatas == null) {
            submitExtendDatas = Collections.emptyList();
        }
        return this;
    }

}

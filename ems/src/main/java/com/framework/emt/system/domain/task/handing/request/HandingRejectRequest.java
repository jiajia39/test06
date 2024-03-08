package com.framework.emt.system.domain.task.handing.request;

import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.record.constant.enums.TaskRecordNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常任务处理驳回
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class HandingRejectRequest implements Serializable {


    @NotNull(message = "驳回节点不能为空")
    @EnumValidator(enumClazz = TaskRecordNode.class, message = "驳回状态错误")
    @ApiModelProperty(value = "驳回节点", required = true)
    private Integer rejectNode;

    @NotBlank(message = "驳回原因不能为空")
    @Length(max = 500, message = "驳回原因长度限制{max}")
    @ApiModelProperty(value = "驳回原因", required = true)
    private String rejectReason;

}

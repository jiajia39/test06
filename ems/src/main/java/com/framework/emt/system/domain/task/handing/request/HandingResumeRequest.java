package com.framework.emt.system.domain.task.handing.request;


import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.exception.task.handing.constant.enums.TaskResumeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常任务 处理挂起恢复 参数
 *
 * @author gaojia
 * @since 2023-08-16
 */
@Getter
@Setter
public class HandingResumeRequest implements Serializable {
    @ApiModelProperty(value = "异常恢复方式 0未恢复 1自动 2 手动")
    @EnumValidator(enumClazz = TaskResumeType.class, message = "异常恢复方式")
    @NotNull(message = "异常恢复方式不能为空")
    private Integer taskResume;
}

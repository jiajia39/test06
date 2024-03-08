package com.framework.emt.system.domain.task.handing.request;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 异常任务挂起延期请求参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class HandingSuspendDelayRequest implements Serializable {

    @NotBlank(message = "延期原因不能为空")
    @Length(max = 100, message = "延期原因长度限制{max}")
    @ApiModelProperty(value = "延期原因", required = true)
    private String reason;

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @ApiModelProperty(value = "延期恢复时间", required = true)
    @NotNull(message = "延期恢复时间不能为空")
    @Future(message = "延期时间不能小于当前时间")
    private LocalDateTime resumeTime;

    public long validateTime(LocalDateTime lastResumeTime) {
        long suspendDelaySeconds = LocalDateTimeUtil.between(lastResumeTime, resumeTime, ChronoUnit.SECONDS);
        if (suspendDelaySeconds <= 0) {
            throw new ServiceException("预计恢复时间不能小于上次预计恢复时间");
        }
        if (suspendDelaySeconds <= ExceptionTaskConstant.SUSPEND_MIN_SECONDS) {
            throw new ServiceException("预计恢复时间至少设置10分钟");
        }
        return suspendDelaySeconds;
    }

}

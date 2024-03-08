package com.framework.emt.system.domain.task.handing.request;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.constant.ExceptionTaskConstant;
import com.framework.emt.system.infrastructure.exception.task.task.request.ExtendFieldsRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 异常任务挂起请求参数
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@Setter
public class HandingSuspendRequest implements Serializable {

    @NotBlank(message = "挂起原因不能为空")
    @Length(max = 100, message = "挂起原因长度限制{max}")
    @ApiModelProperty(value = "挂起原因", required = true)
    private String reason;

    @ApiModelProperty(value = "挂起附件")
    private List<FileRequest> suspendFiles;

    @ApiModelProperty(value = "挂起附加数据")
    @Size(max = 50, message = "挂起附加数据最多{max}个")
    private List<@Valid ExtendFieldsRequest> suspendExtendDatas;

    @NotNull(message = "预计恢复时间不能为空")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @ApiModelProperty(value = "预计恢复时间")
    @Future(message = "预计恢复时间不能小于当前时间")
    private LocalDateTime resumeTime;

    public long validateTime(LocalDateTime now) {
        long suspendSeconds = LocalDateTimeUtil.between(now, resumeTime, ChronoUnit.SECONDS);
        if (suspendSeconds <= 0) {
            throw new ServiceException("预计恢复时间不能小于当前时间");
        }
        if (suspendSeconds <= ExceptionTaskConstant.SUSPEND_MIN_SECONDS) {
            throw new ServiceException("预计恢复时间至少设置10分钟");
        }
        return suspendSeconds;
    }

}

package com.framework.emt.system.domain.statistics.request;

import com.framework.emt.system.domain.statistics.constant.enums.StatisticsType;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统计趋势信息 查询条件
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsTrendQueryRequest implements Serializable {
    @ApiModelProperty(value = "类型 1天 2周 3月 4年", required = true)
    @NotNull(message = "类型不能为空")
    @EnumValidator(enumClazz = StatisticsType.class, message = "类型 1天 2周 3月 4年")
    private Integer type;

    @ApiModelProperty(value = "部门")
    private Long deptId;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}

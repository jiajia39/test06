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
 * 异常趋势折线图 查询条件
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsExceptionTrendQueryRequest implements Serializable {
    @ApiModelProperty(value = "类型 1天 2周 3月", required = true)
    @NotNull(message = "类型不能为空")
    @EnumValidator(enumClazz = StatisticsType.class, message = "类型 1天 2周 3月")
    private Integer type;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;
}

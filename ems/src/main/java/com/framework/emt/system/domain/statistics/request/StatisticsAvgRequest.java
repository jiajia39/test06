package com.framework.emt.system.domain.statistics.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计时间筛选
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsAvgRequest implements Serializable {
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startDate;
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "用户id")
    @Size(max = 5, message = "人员最多{max}个")
    private List<Long> userIds;

    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;

    @ApiModelProperty(value = "类型 1平均响应时间 2.平均处理时间 3.平均验收时间")
    @Range(max = 3, min = 1, message = "类型最大{max}，最小{min}")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "部门id")
    private Long deptId;
}

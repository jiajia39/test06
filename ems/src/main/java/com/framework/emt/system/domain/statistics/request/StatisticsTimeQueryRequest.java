package com.framework.emt.system.domain.statistics.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.statistics.constant.enums.StatisticsType;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
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
public class StatisticsTimeQueryRequest implements Serializable {
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startDate;
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "作业单元id")
    private Long workspaceLocationId;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "任务状态")
    private Long taskStatus;

    @ApiModelProperty(value = "用户id")
    @Size(max = 5, message = "人员最多{max}个")
    private List<Long> userId;

    @ApiModelProperty(value = "异常流程id")
    private Long exceptionProcessId;
}

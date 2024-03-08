package com.framework.emt.system.domain.statistics.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.statistics.constant.enums.StatisticsType;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计部门看板 查询条件
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsDeptBoardRequest extends Query implements Serializable {
    @ApiModelProperty(value = "部门id")
    private Long deptId;


    @ApiModelProperty(value = "异常流程id")
    private List<Long> processIdList;
}

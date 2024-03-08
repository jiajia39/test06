package com.framework.emt.system.domain.statistics.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统计汇总信息 查询条件
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsSummaryQueryRequest implements Serializable {
    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

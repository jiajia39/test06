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
 * 异常关闭率 查询条件
 *
 * @author gaojia
 * @since 2023-08-23
 */
@Getter
@Setter
public class StatisticsCompleteQueryRequest implements Serializable {

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常项id")
    private Long exceptionItemId;
}

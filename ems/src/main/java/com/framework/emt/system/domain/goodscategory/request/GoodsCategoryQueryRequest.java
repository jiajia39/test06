package com.framework.emt.system.domain.goodscategory.request;

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
public class GoodsCategoryQueryRequest implements Serializable {
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "父分类id")
    private Long parentCategoryId;

}

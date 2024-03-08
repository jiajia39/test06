package com.framework.emt.system.domain.goods.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 异常分类 查询参数
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@ApiModel(value = "异常分类树形查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChatQuery extends Query implements Serializable {

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endDate;
}



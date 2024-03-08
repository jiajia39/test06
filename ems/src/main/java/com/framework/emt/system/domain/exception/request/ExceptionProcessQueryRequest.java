package com.framework.emt.system.domain.exception.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 异常流程 查询条件
 *
 * @author ds_C
 * @since 2023-07-20
 */
@Getter
@Setter
public class ExceptionProcessQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "流程名称长度限制{max}")
    @ApiModelProperty(value = "流程名称")
    private String title;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "响应超时上报流程id")
    private Long responseReportNoticeProcessId;

    @ApiModelProperty(value = "处理超时上报流程id")
    private Long handingReportNoticeProcessId;
    @ApiModelProperty(value = "异常项是空的是否展示 0不展示")
    private Integer isShowItemIsNull;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;
}

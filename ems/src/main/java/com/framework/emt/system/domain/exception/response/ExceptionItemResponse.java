package com.framework.emt.system.domain.exception.response;

import com.framework.emt.system.domain.exception.convert.constant.enums.PriorityEnum;
import com.framework.emt.system.domain.exception.convert.constant.enums.SeverityEnum;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 异常项 响应体
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionItemResponse extends BaseUserResponse implements Serializable {

    @ApiModelProperty(value = "异常项id")
    private Long id;

    @ApiModelProperty(value = "异常项名称")
    private String title;

    @ApiModelProperty(value = "异常分类id")
    private Long exceptionCategoryId;

    @ApiModelProperty(value = "异常分类名称")
    private String exceptionCategoryName;

    @ApiModelProperty(value = "异常分类父级ID路径")
    private String categoryParentIdPath;

    @ApiModelProperty(value = "异常分类父级列表")
    private List<String> categoryParentList;

    @ApiModelProperty(value = "异常项状态 0:禁用 1:启用")
    private EnableFlagEnum enableFlag;

    @ApiModelProperty(value = "紧急程度 0:轻微 1:一般 2:紧急 3:特急")
    private PriorityEnum priority;

    @ApiModelProperty(value = "严重程度 0:轻微 1:一般 2:严重 3:致命")
    private SeverityEnum severity;

    @ApiModelProperty(value = "响应时限 单位:分钟")
    private Integer responseDurationTime;

    @ApiModelProperty(value = "处理时限 单位:分钟")
    private Integer handingDurationTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

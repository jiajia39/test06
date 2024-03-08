package com.framework.emt.system.domain.reportnoticeprocess.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 上报流程 查询条件
 *
 * @author ds_C
 * @since 2023-07-17
 */
@Getter
@Setter
public class ReportNoticeProcessQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "上报流程名称长度限制{max}")
    @ApiModelProperty(value = "上报流程名称")
    private String name;

    @ApiModelProperty(value = "关联异常分类")
    private Long exceptionCategoryId;

    @EnumValidator(enumClazz = EnableFlagEnum.class, message = "状态类型错误 0:禁用 1:启用")
    @ApiModelProperty(value = "上报流程状态 0:禁用 1:启用")
    private Integer enableFlag;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

}

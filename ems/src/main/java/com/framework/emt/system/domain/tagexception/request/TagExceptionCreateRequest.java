package com.framework.emt.system.domain.tagexception.request;

import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 异常标签关联 创建参数
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagExceptionCreateRequest implements Serializable {
    @ApiModelProperty(value = "异常标签id", required = true)
    private Long tagId;
    @NotBlank(message = "异常标签信息不能为空")
    @ApiModelProperty(value = "异常标签信息", required = true)
    private String content;
    @NotNull(message = "关联表id不能为空")
    @ApiModelProperty(value = "关联表id", required = true)
    private Long sourceId;
    @NotNull(message = "异常关联类型不能为空")
    @EnumValidator(enumClazz = SourceTypeEnum.class, message = "异常关联类型 0知识库 1异常流程 2异常任务")
    @ApiModelProperty(value = "异常关联类型 0知识库 1异常流程 2异常任务", required = true)
    private Integer sourceType;

}

package com.framework.emt.system.domain.tagexception.request;

import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常标签关联 删除参数
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagExceptionDeleteRequest implements Serializable {

    @NotNull(message = "关联表id不能为空")
    @ApiModelProperty(value = "关联表id", required = true)
    private Long sourceId;

}

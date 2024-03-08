package com.framework.emt.system.domain.exception.request;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常流程标签关系 创建参数
 *
 * @author ds_C
 * @since 2023-08-03
 */
@Getter
@Setter
public class ExceptionProcessTagRelationCreateRequest implements Serializable {

    @NotNull(message = "异常流程id不能为空")
    @ApiModelProperty(value = "异常流程id", required = true)
    private Long id;

    @ApiModelProperty(value = "异常原因项标签id")
    private Long tagId;

    @Length(max = 20, message = "异常流程原因项标签内容长度限制{max}")
    @ApiModelProperty(value = "异常流程原因项标签内容")
    private String tagContent;

    public ExceptionProcessTagRelationCreateRequest init() {
        if (ObjectUtil.isNotNull(tagId)) {
            tagContent = null;
        }
        if (StringUtils.isNotBlank(tagContent)) {
            tagId = null;
        }
        return this;
    }

}

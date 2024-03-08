package com.framework.emt.system.domain.formfieldUse.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文本输入框 实体类
 *
 * @author gao
 * @since 2024-01-31 18:20:10
 */
@Data
public class InputResponse implements Serializable {

    @ApiModelProperty(value = "内容")
    private String content;
}



package com.framework.emt.system.domain.formfieldUse.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 选择 实体类
 *
 * @author gao
 * @since 2024-01-31 18:20:10
 */
@Data
public class SelectResponse implements Serializable {

    @ApiModelProperty(value = "选择的选项")
    private String key;

    @ApiModelProperty(value = "中文名")
    private String value;
}



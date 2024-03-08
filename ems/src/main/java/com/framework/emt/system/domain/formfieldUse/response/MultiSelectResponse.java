package com.framework.emt.system.domain.formfieldUse.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 多选框 实体类
 *
 * @author gao
 * @since 2024-01-31 18:20:10
 */
@Data
public class MultiSelectResponse implements Serializable {

    @ApiModelProperty(value = "内容")
    private List<SelectResponse> selectList;
}



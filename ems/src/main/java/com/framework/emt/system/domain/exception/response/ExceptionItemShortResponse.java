package com.framework.emt.system.domain.exception.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常项简化信息
 * @author jiaXue
 * date 2023/10/26
 */
@Data
public class ExceptionItemShortResponse implements Serializable {

    @ApiModelProperty(value = "异常分类id")
    private Long categoryId;

    @ApiModelProperty(value = "异常项名称")
    private String title;


}

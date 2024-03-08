package com.framework.emt.system.domain.tagexception.response;

import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常标签关联 响应体
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagExceptionResponse extends BaseUserResponse implements Serializable {
    @ApiModelProperty(value = "异常表单字段id")
    private Long id;

    @ApiModelProperty(value = "异常标签id")
    private Long tagId;

    @ApiModelProperty(value = "关联表id")
    private Long sourceId;

    @ApiModelProperty(value = "异常关联类型 0知识库 1异常流程 2异常任务")
    private Integer sourceType;
}

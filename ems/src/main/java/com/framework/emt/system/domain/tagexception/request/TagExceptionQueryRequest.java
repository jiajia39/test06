package com.framework.emt.system.domain.tagexception.request;

import com.framework.core.mybatisplus.support.Query;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.common.validation.EnumValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 异常标签关联 查询条件
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@Setter
public class TagExceptionQueryRequest extends Query implements Serializable {
    @ApiModelProperty(value = "异常关联类型 0知识库 1异常流程 2异常任务")
    private Integer sourceType;
}

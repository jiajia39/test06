package com.framework.emt.system.domain.exception.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 异常分类 查询条件
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@Setter
public class ExceptionCategoryQueryRequest extends Query implements Serializable {

    @Length(max = 20, message = "异常分类名称长度限制{max}")
    @ApiModelProperty(value = "异常分类名称")
    private String title;

    @ApiModelProperty(value = "异常分类父ID 顶级异常分类父ID为0")
    private Long parentId;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

}

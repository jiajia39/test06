package com.framework.emt.system.domain.goods.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品 查询参数
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@ApiModel(value = "商品查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GoodsQuery extends Query implements Serializable {
    @ApiModelProperty(value = "商品名称")
    @Length(max = 50, message = "商品名称长度不能大于{max}")
    private String name;

    @ApiModelProperty(value = "商品分类id")
    private Long categoryId;
    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    @Length(max = 20, message = "商品编号长度不能大于{max}")
    private String code;
}



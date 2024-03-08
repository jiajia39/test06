package com.framework.emt.system.domain.goodscategory.request;

import com.framework.core.mybatisplus.support.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 异常分类 查询参数
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@ApiModel(value = "异常分类查询参数")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GoodsCategoryQuery extends Query implements Serializable {

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @Length(max = 20, message = "分类名称长度不能大于{max}")
    private String title;

    @ApiModelProperty(value = "异常分类父ID 顶级异常分类父ID为0")
    private Long parentId;

    @ApiModelProperty(value = "编号")
    @Length(max = 20, message = "分类编号 保留字段长度不能大于{max}")
    public String code;

    @ApiModelProperty(value = "是否显示")
    @Range(max = 1,min = 0,message = "是否显示最大值1显示 0不显示")
    public Integer enableFlag;
}



package com.framework.emt.system.domain.goods.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 商品 新增参数
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@ApiModel(value = "商品新增参数")
@Data
public class GoodsCreateRequest implements Serializable {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = true)
    @Length(max = 50, message = "商品名称长度不能大于{max}")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "商品分类id", required = true)
    @NotNull(message = "商品分类id不能为空")
    private Long categoryId;

    /**
     * 商品商品路径id
     */
    @JsonIgnore
    private String categoryParentIdPath;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号", required = true)
    @NotBlank(message = "商品编号不能为空")
    @Length(max = 20, message = "商品编号长度不能大于{max}")
    private String code;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格", required = true)
    @NotNull(message = "价格不能为空")
    @Range()
    private Double price;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank(message = "单位不能为空")
    @Length(max = 50, message = "单位长度不能大于{max}")
    private String unit;

    /**
     * 是否上架 0:未上架 1:已上架
     */
    @ApiModelProperty(value = "是否上架 0:未上架 1:已上架", required = true)
    @Range(max = 1, min = 0, message = "是否上架最大值是${max},最小值是${min}")
    @NotNull(message = "是否上架不能为空")
    private Integer enableListing;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    private Date listingTime;

    /**
     * 是否推荐 0未推荐 1推荐
     */
    @ApiModelProperty(value = "是否推荐 0未推荐 1推荐", required = true)
    @Range(max = 1, min = 0, message = "是否推荐最大值是${max},最小值是${min}")
    @NotNull(message = "是否推荐不能为空")
    private Integer isRecommend;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 500, message = "描述长度不能大于{max}")
    private String goodsDescribe;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "备注长度不能大于{max}")
    private String remark;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片", required = true)
    @NotEmpty(message = "商品图片不能为空")
    private List<FileRequest> goodsImg;

}
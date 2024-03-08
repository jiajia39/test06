package com.framework.emt.system.domain.goods.response;

import com.framework.emt.system.domain.goodscategory.GoodsCategory;
import com.framework.emt.system.domain.goodscategory.response.GoodsCategoryResponse;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.common.response.BaseUserResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 商品 返回参数
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@ApiModel(value = "商品返回参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsResponse extends BaseUserResponse {
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String code;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 是否上架 0:未上架 1:已上架
     */
    @ApiModelProperty(value = "是否上架 0:未上架 1:已上架")
    private Integer enableListing;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    private Date listingTime;

    /**
     * 是否推荐 0未推荐 1推荐
     */
    @ApiModelProperty(value = "是否推荐 0未推荐 1推荐")
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
    private String goodsDescribe;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private List<FileRequest> goodsImg;

    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "分类信息")
    private String categoryName;

    @ApiModelProperty(value = "商品分类父级ID路径")
    private String categoryParentIdPath;

    @ApiModelProperty(value = "商品分类父级列表")
    private List<String> categoryParentList;
}



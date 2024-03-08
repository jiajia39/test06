package com.framework.emt.system.domain.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品 实体类
 *
 * @author makejava
 * @since 2024-02-22 14:58:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "goods",autoResultMap = true)
public class Goods extends TenantEntity {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品商品路径id parentId1_parentId2_parentId3
     */
    private String categoryParentIdPath;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 价格
     */
    private Double price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否上架 0:未上架 1:已上架
     */
    private Integer enableListing;

    /**
     * 上架时间
     */
    private Date listingTime;

    /**
     * 是否推荐 0未推荐 1推荐
     */
    private Integer isRecommend;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String goodsDescribe;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商品图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> goodsImg = new ArrayList<>();

    /**
     * 分类id
     */
    private Long categoryId;

}



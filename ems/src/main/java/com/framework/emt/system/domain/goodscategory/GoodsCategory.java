package com.framework.emt.system.domain.goodscategory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常分类 实体类
 *
 * @author makejava
 * @since 2024-02-22 13:24:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "goods_category", autoResultMap = true)
public class GoodsCategory extends TenantEntity {

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 父级ID路径 parentId1_parentId2_parentId3
     */
    private String parentIdPath;

    /**
     * 分类名称
     */
    private String title;

    /**
     * 分类编号 保留字段
     */
    private String code;

    /**
     * 状态 0:禁用 1:启用。保留字段
     */
    private Integer enableFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分类图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> categoryImg;

    /**
     * 层级
     */
    private Integer level;


}



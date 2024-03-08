package com.framework.emt.system.domain.knowledge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识库分类 实体类
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emt_knowledge_base_category")
public class KnowledgeBaseCategory extends TenantEntity {

    /**
     * 知识库分类名称
     */
    private String title;

    /**
     * 知识库分类编号 保留字段
     */
    private String code;

    /**
     * 状态 0:禁用 1:启用
     */
    private EnableFlagEnum enableFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

}

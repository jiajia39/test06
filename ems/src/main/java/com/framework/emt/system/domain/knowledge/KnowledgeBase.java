package com.framework.emt.system.domain.knowledge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.framework.core.tenant.entity.TenantEntity;
import com.framework.emt.system.infrastructure.common.request.FileRequest;
import com.framework.emt.system.infrastructure.constant.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 知识库 实体类
 *
 * @author ds_C
 * @since 2023-07-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "emt_knowledge_base", autoResultMap = true)
public class KnowledgeBase extends TenantEntity {

    /**
     * 知识库分类id
     */
    private Long knowledgeBaseCategoryId;

    /**
     * 异常项id
     */
    private Long exceptionItemId;

    /**
     * 知识库名称
     */
    private String title;

    /**
     * 知识库编号 保留字段
     */
    private String code;

    /**
     * 问题描述
     */
    private String problemDescription;

    /**
     * 原因分析
     */
    private String problemAnalysis;

    /**
     * 防止再发措施
     */
    private String preventRecurrenceMeasure;

    /**
     * 解决建议
     */
    private String suggestion;

    /**
     * 文件列表 默认最多10个
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FileRequest> files;

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

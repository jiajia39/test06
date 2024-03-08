package com.framework.emt.system.domain.knowledge.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 知识库分类异常码
 * code为2400-2500
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum KnowledgeBaseCategoryErrorCode implements IResultCode {

    /**
     * 异常码
     */
    KNOWLEDGE_BASE_CATEGORY_INFO_NOT_FIND(2400, "知识库分类信息未查询到数据"),
    KNOWLEDGE_BASE_CATEGORY_CONTAIN_KNOWLEDGE_BASE(2401, "删除失败，知识库分类下包含知识库无法删除"),
    KNOWLEDGE_BASE_CATEGORY_TITLE_IS_EXIST(2402, "知识库分类名称已存在"),
    KNOWLEDGE_BASE_CATEGORY_TITLE_IS_NOT_BLANK(2403, "知识库分类名称不能为空"),
    EXCEl_CONTAIN_NOT_EXIST_KNOWLEDGE_BASE_CATEGORY_TITLE(2404, "Excel中包含不存在的知识库分类名称，请你检查之后重试！");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

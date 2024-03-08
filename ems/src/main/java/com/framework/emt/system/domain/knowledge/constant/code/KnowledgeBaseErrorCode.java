package com.framework.emt.system.domain.knowledge.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 知识库异常码
 * code为2500-2600
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum KnowledgeBaseErrorCode implements IResultCode {

    /**
     * 异常码
     */
    KNOWLEDGE_BASE_INFO_NOT_FIND(2500, "知识库信息未查询到数据"),
    KNOWLEDGE_BASE_TITLE_IS_EXIST(2501, "知识库标题已存在"),
    KNOWLEDGE_BASE_TAG_ID_OR_TAG_CONTENT_MUST_HAVE_ONE(2502, "知识库关键词标签id或标签名称必须填写一个"),
    KNOWLEDGE_BASE_TITLE_IS_NOT_BLANK(2503, "知识库标题不能为空"),
    KNOWLEDGE_BASE_PROBLEM_DESCRIPTION_IS_NOT_BLANK(2504, "问题描述不能为空"),
    KNOWLEDGE_BASE_PROBLEM_ANALYSIS_IS_NOT_BLANK(2505, "原因分析不能为空"),
    KNOWLEDGE_BASE_SUGGESTION_IS_NOT_BLANK(2506, "解决方案不能为空"),
    KNOWLEDGE_BASE_KEYWORD_MUST_UNIQUE(2507, "每条知识库对应的关键词不能重复，请你检查之后重试！"),
    KNOWLEDGE_BASE_TITLE_MUST_UNIQUE(2508, "知识库标题不能重复");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

package com.framework.emt.system.domain.tagexception.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签关联异常码
 * code为3300-3400
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@AllArgsConstructor
public enum TagExceptionErrorCode implements IResultCode {

    /**
     * 异常码
     */
    EXCEPTION_TAG_ASSOCIATION_INFORMATION_ALREADY_EXISTS(3300, "异常标签关联信息已存在"),
    SOURCE_ID_NOT_EXISTS(3301, "关联id的异常标签关联信息不存在"),
    TAG_EXCEPTION_INFO_NOT_FIND(3302, "异常标签关联信息未查询到数据"),
    NOT_EXIST_DATA_CAN_NOT_DELETE(3303, "删除失败，不存在的数据无法删除"),
    SOURCE_TYPE_IS_KNOWLEDGE_BASE_THE_SOURCE_ID_MUST_BE_KNOWLEDGE_BASE_DATA(3304, "新增失败，标签关联类型是知识库时，关联表id必须是知识库数据"),
    SOURCE_TYPE_IS_ABNORMAL_PROCESS_THE_SOURCE_ID_MUST_BE_ABNORMAL_PROCESS_DATA(3305, "新增失败，标签关联类型是异常流程时，关联表id必须是异常流程表数据"),
    THE_TYPE_OF_TAG_ID_LIST_DOES_NOT_MATCH_THE_SOURCE_TYPE(3306, "异常标签id列表的类型和异常关联类型不符"),
    THE_TYPE_OF_TAG_ID_NOT_MATCH_THE_SOURCE_TYPE(3307, "异常标签id的类型和异常关联类型不符");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

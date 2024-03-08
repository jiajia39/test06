package com.framework.emt.system.domain.tag.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签异常码
 * code为3200-3300
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@AllArgsConstructor
public enum TagErrorCode implements IResultCode {

    /**
     * 异常码
     */
    EXCEPTION_TAG_CONTENT_ALREADY_EXISTS(3200, "异常标签内容已存在"),
    TAG_INFO_NOT_FIND(3201, "异常标签信息未查询到数据"),
    NOT_EXIST_DATA_CAN_NOT_DELETE(3202, "删除失败，不存在的数据无法删除");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

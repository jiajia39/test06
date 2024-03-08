package com.framework.emt.system.domain.messages.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息异常码
 * code为2700-2800
 *
 * @author gaojia
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum MessageErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NO_DATA_FOUND_IN_THE_MESSAGE(2700, "消息未查询到数据"),
    TEMPLATE_NOT_FOUND(2701, "未查询到消息模板");;

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

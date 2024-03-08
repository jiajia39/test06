package com.framework.emt.system.domain.formfieldUse.constant;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常单字段使用表 异常码
 *
 * @author makejava
 * @since 2024-01-31 18:20:10
 */
@Getter
@AllArgsConstructor
public enum FormFieldUseErrorCode implements IResultCode {

    /**
     * 异常码
     */
    IS_EXIST(1001, "异常单字段使用表已存在"),
    NOT_FOUND(1002, "异常单字段使用表未查询到"),
    DELETE_NOT_EXIST(1003, "删除失败，异常单字段使用表未查询到");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}



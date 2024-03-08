package com.framework.center.infrastructure.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用异常码
 * 1100-1199
 *
 * @author yankunw
 * date 2023-04-13
 */

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements IResultCode {

    /**
     * 通用异常码
     */
    ACCOUNT_ALREADY_EXISTS(1024, "账户已经存在！"),
    THE_SELECTED_COMPANY_WAS_NOT_FOUND(1025, "未找到所选择的所属公司！"),
    THE_PASSWORDS_ENTERED_TWICE_DO_NOT_MATCH(1026, "两次输入的密码不一致!"),
    NOT_FOUND(1027, "未查询到数据"),
    CREATE_ERROR(1028, "新增数据失败"),
    UPDATE_ERROR(1029, "更新数据失败"),
    DELETE_ERROR(1030, "删除数据失败"),
    ACCOUNT_DOES_NOT_EXIST(1031, "账户不存在！"),
    LOGIN_FAILED(401, "登录失败：原因访问统一门户接口失败");


    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}

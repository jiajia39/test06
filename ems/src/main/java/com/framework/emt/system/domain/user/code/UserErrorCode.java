package com.framework.emt.system.domain.user.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户异常码
 * code为2300-2400
 *
 * @author ds_C
 * @since 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements IResultCode {

    /**
     * 异常码
     */
    RECEIVER_USER_INFO_NOT_FIND(2300, "上报人信息未查询到数据"),
    RESPONSE_USER_LIST_CONTAIN_NOT_EXIST_RESPONSE_USER(2301, "响应人列表包含了不存在的响应人，请你检查之后重试！"),
    HANDING_USER_LIST_CONTAIN_NOT_EXIST_HANDING_USER(2302, "处理人列表包含了不存在的处理人，请你检查之后重试！"),
    CHECK_USER_LIST_CONTAIN_NOT_EXIST_CHECK_USER(2303, "验收人列表包含了不存在的验收人，请你检查之后重试！"),
    CHECK_RECIPIENT_USER_LIST_CONTAIN_NOT_EXIST_RECIPIENT_USER(2304, "验收成功通知人列表包含了不存在的通知人，请你检查之后重试！"),
    TRANSFER_USER_INFO_NOT_FIND(2305, "转派人信息未查询到数据");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

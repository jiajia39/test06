package com.framework.emt.system.infrastructure.exception.task.response.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常响应异常码
 * code为3500-3600
 *
 * @author gaojia
 * @since 2023-08-08
 */
@Getter
@AllArgsConstructor
public enum TaskResponseErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(3500, "未查询到异常响应信息"),
    TASK_RESPONSE_INFO_NOT_FIND(3501, "异常响应信息未查询到数据"),
    THE_RESPONSE_INFORMATION_OF_THE_OPERATOR_DOES_NOT_EXIST(3501, "当前操作人的响应信息不存在"),
    NOT_EXIST_DATA_CAN_NOT_DELETE(3202, "删除失败，不存在的数据无法删除"),
    WHEN_ADDING_A_RESPONSE_THE_ABNORMAL_STATE_MUST_BE_PENDING_RESPONSE(3503, "新增响应时，异常状态必须是待响应"),

    WHEN_SETTING_THE_HANDLER_THE_EXCEPTION_STATE_MUST_BE_RESPONDING(3504, "设置处理人时，异常状态必须是响应中"),
    WHEN_RESPONDING_TO_REJECTION_THE_ABNORMAL_STATUS_MUST_BE_EITHER_PENDING_RESPONSE_OR_TRANSFERRED(3506, "异常状态必须是待响应或已转派"),
    THERE_IS_ADDITIONAL_INFORMATION_THAT_DOES_NOT_BELONG_TO_THE_RESPONSE_ADDITIONAL_FIELD(3507, "附加信息错误请核查"),

    THE_PROCESSOR_IS_NOT_IN_THE_PRE_CONFIGURED_PROCESSOR_LIST_PLEASE_CHECK(3508, "处理人未在提前配置的处理人列表中，请检查"),
    NOT_PRE_HANDING(3509, "异常任务不是待处理状态"),
    NOT_HANDING(3510, "异常任务不是处理中或者已驳回状态"),
    NOT_SUSPEND(3511, "异常任务不是挂起状态"),
    ABOVE_SUSPEND_MAX_COUNT(3512, "异常挂起次数已达到最大值"),
    ABOVE_SUSPEND_MAX_MINUTES(3513, "异常挂起时间已达到最大值"),
    ABNORMAL_REASON_ITEM_ERROR_PLEASE_CHECK(3514, "异常原因项错误请核查"),
    THE_RESPONSE_SENDER_CANNOT_BE_SET_AS_THEMSELVES(3515, "响应转派人不能设置成自己"),
    NO_RESPONSE_TRANSFEROR_FOUND(3516, "未查询到响应转派人"),
    THE_MAXIMUM_NUMBER_OF_RESPONSE_TRANSFERS_HAS_BEEN_REACHED(3517, "响应转派次数已达到最大值"),
    CURRENT_USER_DOES_NOT_HAVE_ABNORMAL_MANAGEMENT_RESPONSE_TRANSFER_PERMISSION(3518, "当前用户没有异常管理响应转派权限"),
    THE_CURRENT_USER_DOES_NOT_HAVE_ABNORMAL_MANAGEMENT_RESPONSE_TRANSFER_PERMISSION(3519, "响应转派人不能是当前响应人"),
    ;


    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

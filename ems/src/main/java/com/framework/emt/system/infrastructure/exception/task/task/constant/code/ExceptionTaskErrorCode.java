package com.framework.emt.system.infrastructure.exception.task.task.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常任务异常码
 * code为3500-3600
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@AllArgsConstructor
public enum ExceptionTaskErrorCode implements IResultCode {

    /**
     * 异常码
     */
    TASK_INFO_NOT_FIND(3500, "异常任务信息未查询到数据"),
    SETTING_NOT_FOUND(3501, "未查询到异常配置"),
    HANDING_USER_NOT_FOUND(3503, "未查询到处理人"),
    HANDING_USER_NOT_SAME(3504, "处理人与固定人员不是同一人"),
    HANDING_USER_NOT_IN_LIST(3505, "处理人不在处理人列表中"),
    CHECK_USER_LIST_NOT_NULL(3606, "验收人id列表不能为空"),
    CHECK_USER_PART_NOT_EXIST(3507, "部分验收人不存在"),
    CHECK_USER_NOT_SAME(3508, "验收人与固定人员不是同一人"),
    CHECK_USER_PART_NOT_IN_LIST(3509, "部分验收人不在验收人列表中"),
    CHECK_DATA_NOT_FOUND(3510, "未查询到异常配置验收数据"),
    HANDING_DATA_NOT_FOUND(3511, "未查询到异常配置处理数据"),
    RESPONSE_HANDLER_CANNOT_BE_EMPTY(3512, "响应处理人不能为空"),
    ADDITIONAL_FIELDS_ARE_MISSING_PLEASE_CHECK(3513, "附加字段缺失，请检查"),
    THE_TASK_HAS_BEEN_COMPLETED_AND_CANNOT_BE_ABORTED(3514, "任务已完成，不能进行中止操作"),
    THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_CLOSE_EXCEPTION_MANAGEMENT(3515, "当前用户没有异常管理关闭权限");

    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

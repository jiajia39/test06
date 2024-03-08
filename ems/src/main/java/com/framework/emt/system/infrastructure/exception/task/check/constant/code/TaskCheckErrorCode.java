package com.framework.emt.system.infrastructure.exception.task.check.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常任务验收异常码
 * code为4100-4200
 *
 * @author gaojia
 * @since 2023-08-10
 */
@Getter
@AllArgsConstructor
public enum TaskCheckErrorCode implements IResultCode {

    /**
     * 异常码
     */

    TASK_CHECK_INFO_NOT_FIND(4100, "异常验收信息未查询到数据"),
    ADDING_ACCEPTANCE_INFORMATION_THE_ABNORMAL_STATE_MUST_BE_PENDING_ACCEPTANCE(4101, "新增验收信息时，异常状态必须是待验收"),
    THE_ACCEPTANCE_INFORMATION_STATUS_MUST_BE_PENDING_ACCEPTANCE(4102, "验收信息状态必须是待验收"),
    THE_INSPECTOR_AND_OPERATOR_DO_NOT_MATCH(4103, "验收人和操作人不符，不能进行相关操作"),
    THERE_IS_ADDITIONAL_INFORMATION_THAT_DOES_NOT_BELONG_TO_THE_RESPONSE_ADDITIONAL_FIELD(4104, "附加信息错误请核查"),
    ACCEPTANCE_INFORMATION_MUST_BE_VALID(4105, "验收信息必须是生效的"),
    THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_PASS_EXCEPTION_MANAGEMENT_ACCEPTANCE(4106, "当前用户没有异常管理验收通过的权限"),

    THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_REJECT_EXCEPTION_MANAGEMENT_ACCEPTANCE(4107, "当前用户没有异常管理验收驳回的权限"),
    ;;
    /**
     * code编码
     */
    final int code;

    /**
     * 中文信息描述
     */
    final String message;

}

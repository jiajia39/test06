package com.framework.emt.system.infrastructure.exception.task.submit.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常提报 异常码
 * code为3000-3100
 *
 * @author gaojia
 * @since 2023-08-02
 */
@Getter
@AllArgsConstructor
public enum ExceptionTaskSubmitErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(3000, "异常提报信息未查询到数据"),
    EXCEPTION_TASK_SUBMIT_STATUS_MUST_DRAFT_CAN_DELETE(3001, "异常提报主状态必须是待提交、已撤销、已关闭时才能删除"),
    ONLY_CREATE_USER_CAN_DELETE_UPDATE_CLOSE_OR_SUBMIT(3002, "异常提报信息只能由此条异常提报的创建人删除、修改、关闭或提报"),
    EXCEPTION_TASK_SUBMIT_STATUS_MUST_SUBMIT_REJECT_CAN_CLOSE(3003, "异常提报主状态必须是提报已驳回时才能关闭"),
    STATUS_MUST_DRAFT_OR_CANCEL_OR_SUBMIT_REJECT_CAN_UPDATE(3004, "提报状态必须是待提交、已撤销或提报已驳回时才能编辑"),
    STATUS_MUST_DRAFT_OR_CANCEL_OR_SUBMIT_REJECT_CAN_SUBMIT(3005, "提报状态必须是待提交、已撤销或提报已驳回时才能提报"),
    RESPONSE_USER_NOT_FOUND(3006, "未查询到响应人"),
    RESPONDENT_ID_CANNOT_BE_EMPTY(3007, "响应人id不能为空"),
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

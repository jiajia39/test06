package com.framework.emt.system.infrastructure.exception.task.cooperation.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常协同 异常码
 * code为3000-3100
 *
 * @author
 * @since 2023-08-13
 */
@Getter
@AllArgsConstructor
public enum ExceptionTaskCooperationErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(3000, "异常协同信息未查询到数据"),
    THE_COOPERATION_INFORMATION_STATUS_MUST_BE_COOPERATION_ING(3001, "协同信息状态必须是协同中"),
    THE_COOPERATION_INFORMATION_STATUS_MUST_BE_WAIT_COOPERATION(3002, "协同信息状态必须是待协同"),
    YOU_NOT_IS_THIS_COOPERATION_INFORMATION_OF_COOPERATION_USER(3003, "你不是此条协同信息对应的协同人"),
    COOPERATION_TASK_CAN_NOT_TRANSFER_FOR_ONESELF(3004, "验证协同数据已超过协同任务最大数目"),
    COLLABORATION_INFORMATION_STATUS_MUST_BE_REVOKED(3005, "协同信息状态必须是已撤销"),
    THE_COLLABORATIVE_TASK_OF_THE_PLAN_COLLABORATOR_ALREADY_EXISTS(3006, "计划协同人的协同任务已经存在"),
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

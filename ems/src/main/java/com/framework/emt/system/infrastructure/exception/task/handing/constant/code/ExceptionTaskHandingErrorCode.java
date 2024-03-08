package com.framework.emt.system.infrastructure.exception.task.handing.constant.code;

import com.framework.common.api.entity.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常处理 异常码
 * code为3600-3700
 *
 * @author gaojia
 * @since 2023-08-12
 */
@Getter
@AllArgsConstructor
public enum ExceptionTaskHandingErrorCode implements IResultCode {

    /**
     * 异常码
     */
    NOT_FOUND(3600, "未查询到异常处理信息"),
    NOT_ACCEPT_USER(3601, "当前用户不是异常处理信息处理人"),
    REJECT_NODE_ERROR(3602, "异常处理驳回节点错误"),
    OTHER_USER_NOT_EXIST(3603, "未查询到处理转派人"),
    PLAN_COOPERATION_USER_NOT_EXIST(3604, "未查询到计划协同人"),
    PROCESSING_INFORMATION_STATUS_MUST_BE_IN_PRE_HANDING(3605, "处理信息状态必须是待处理"),
    COLLABORATION_TASKS_NOT_ALL_FINISHED(3606, "协同任务未完成"),
    PROCESSING_INFORMATION_STATUS_MUST_BE_HANDING(3607, "处理信息状态必须是处理中"),
    THE_TRANSFEROR_CANNOT_BE_SET_AS_THEMSELVES(3608, "处理转派人不能设置成自己"),
    COLLABORATORS_CANNOT_BE_SET_AS_THEMSELVES(3609, "协同人不能设置成自己"),
    THE_MAXIMUM_NUMBER_OF_TRANSFERS_PROCESSED_HAS_BEEN_REACHED(3610, "处理转派次数已达到最大值"),
    THE_NUMBER_OF_COLLABORATIVE_TRANSFERS_HAS_REACHED_THE_MAXIMUM_VALUE(3611, "协同转派次数已达到最大值"),
    CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_HANDLE_AND_TRANSFER_EXCEPTIONS(3612, "当前用户没有异常管理处理转派权限"),
    PROCESSING_TRANSFER_PERSON_CANNOT_BE_THE_SAME_AS_THE_CURRENT_PROCESSING_PERSON(3613, "处理转派人不能和当前处理人相同"),
    THE_CURRENT_USER_DOES_NOT_HAVE_PERMISSION_TO_UPDATE_EXCEPTION_MANAGEMENT_PROCESSING(3612, "当前用户没有异常管理处理更新权限"),
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
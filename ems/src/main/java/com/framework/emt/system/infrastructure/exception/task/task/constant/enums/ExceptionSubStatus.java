package com.framework.emt.system.infrastructure.exception.task.task.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常子状态 枚举
 *
 * @Author ds_C
 * @Date 2023-08-14
 */
@Getter
@AllArgsConstructor
public enum ExceptionSubStatus implements BaseEnum<ExceptionSubStatus> {

    /**
     * 主状态-提报节点 子状态
     */
    DRAFT(11, "待提交"),
    CANCEL(21, "已撤销"),
    CLOSE(31, "已关闭"),

    SUBMIT_REJECT_RESPONSE(41, "响应驳回"),
    SUBMIT_REJECT_HANDING(42, "处理驳回"),
    /**
     * 主状态-响应节点 子状态
     */
    PRE_RESPONSE(51, "待响应"),
    PRE_RESPONSE_OTHER(52, "已转派"),
    RESPONSE_REJECT(61, "响应已驳回"),
    RESPONDING(71, "响应中"),
    /**
     * 主状态-处理节点 子状态
     */
    PRE_HANDING(81, "待处理"),
    PRE_HANDING_OTHER(82, "已转派"),
    HANDING_REJECT(91, "处理已驳回"),
    HANDING(101, "处理中"),
    SUSPEND(111, "已挂起"),
    CHECK(121, "待验收"),
    FINISH(131, "已完成");

    /**
     * code编码
     */
    @EnumValue
    @JsonValue
    final Integer code;

    /**
     * 中文信息描述
     */
    final String name;

}

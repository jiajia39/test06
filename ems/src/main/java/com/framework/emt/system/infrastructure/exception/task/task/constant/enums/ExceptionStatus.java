package com.framework.emt.system.infrastructure.exception.task.task.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常状态 枚举
 *
 * @Author ds_C
 * @Date 2023-08-14
 */
@Getter
@AllArgsConstructor
public enum ExceptionStatus implements BaseEnum<ExceptionStatus> {

    /**
     * 主状态-提报节点
     */
    DRAFT(1, "待提交"),
    CANCEL(2, "已撤销"),
    CLOSE(3, "已关闭"),
    SUBMIT_REJECT(4, "提报已驳回"),
    /**
     * 主状态-响应节点
     */
    PRE_RESPONSE(5, "待响应"),
    RESPONSE_REJECT(6, "响应已驳回"),
    RESPONDING(7, "响应中"),
    /**
     * 主状态-处理节点
     */
    PRE_HANDING(8, "待处理"),
    HANDING_REJECT(9, "处理已驳回"),
    HANDING(10, "处理中"),
    SUSPEND(11, "已挂起"),


    CHECK(12, "待验收"),
    FINISH(13, "已完成");

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

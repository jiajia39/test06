package com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协同子状态 枚举
 *
 * @Author ds_C
 * @Date 2023-08-12
 */
@Getter
@AllArgsConstructor
public enum CooperationSubStatus implements BaseEnum<CooperationSubStatus> {

    /**
     * 协同状态-协同节点 子状态
     */
    WAIT_COOPERATION(11, "待协同"),
    ALREADY_TRANSFER(12, "已转派"),
    ALREADY_REVOKED(21, "已撤销"),
    COOPERATION_ING(31, "协同中"),
    ALREADY_COMPLETED(41, "已完成");

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

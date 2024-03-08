package com.framework.emt.system.infrastructure.exception.task.check.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验收子状态 枚举
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum CheckSubStatus implements BaseEnum<CheckSubStatus> {

    /**
     * 验收状态-验收节点 子状态
     */
    WAIT_CHECK(11, "待验收"),
    WAIT_CHECK_EXPIRED(12, "已过期"),
    CHECK_REJECT(21, "验收驳回"),
    CHECK_PASSED(31, "验收通过"),
    CHECK_PASSED_EXPIRED(32, "已过期");

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

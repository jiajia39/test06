package com.framework.emt.system.domain.messages.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 异常类型 枚举
 *
 * @Author ds_C
 * @Date 2023-08-22
 */
@Getter
@AllArgsConstructor
public enum ExceptionType implements BaseEnum<ExceptionType> {

    /**
     * 异常类型
     */
    SUBMIT(1, "异常提报"),
    RESPONSE(2, "异常响应"),
    HANDING(3, "异常处理"),
    COOPERATION(4, "异常协同"),
    CHECK(5, "异常验收");

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

    /**
     * 获取异常响应和异常处理code码
     *
     * @return
     */
    public static List<Integer> getCodes() {
        return Lists.newArrayList(RESPONSE.getCode(), HANDING.getCode());
    }

    /**
     * 校验异常类型code是否是:
     * 异常响应、处理或协同
     *
     * @param code 异常类型code
     * @return
     */
    public static boolean validate(Integer code) {
        return RESPONSE.getCode().equals(code) || HANDING.getCode().equals(code) || COOPERATION.getCode().equals(code);
    }

}

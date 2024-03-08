package com.framework.emt.system.infrastructure.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 禁用启用状态 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum EnableFlagEnum implements BaseEnum<EnableFlagEnum> {

    /**
     * 禁用启用状态
     */
    FORBIDDEN(0, "禁用"),
    ENABLE(1, "启用");

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

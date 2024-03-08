package com.framework.emt.system.domain.exception.convert.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验收条件 枚举
 *
 * @Author gaojia
 * @Date 2023-09-18
 */
@Getter
@AllArgsConstructor
public enum CheckConditionEnum implements BaseEnum<CheckConditionEnum> {

    /**
     * 验收模式
     */
    REQUIRE_EVERYONE_ACCEPTANCE(0, "需要所有人验收"),
    ONLY_ONE_PERSON_IS_REQUIRED_FOR_ACCEPTANCE(1, "仅需单人验收"),
    ;

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
     * 验收条件
     *
     * @param key key
     * @return 验收条件
     */
    public static CheckConditionEnum getEnum(Integer key) {
        for (CheckConditionEnum checkModeEnum : values()) {
            if (ObjectUtil.equals(checkModeEnum.getCode(), key)) {
                return checkModeEnum;
            }
        }
        return null;
    }

}

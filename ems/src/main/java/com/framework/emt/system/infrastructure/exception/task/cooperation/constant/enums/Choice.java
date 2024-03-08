package com.framework.emt.system.infrastructure.exception.task.cooperation.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 选择 枚举
 *
 * @Author ds_C
 * @Date 2023-08-16
 */
@Getter
@AllArgsConstructor
public enum Choice implements BaseEnum<Choice> {

    /**
     * 选择 枚举
     */
    NO(0, "否"),
    YES(1, "是"),
    ALL(2, "全部");

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

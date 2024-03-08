package com.framework.emt.system.domain.formfield.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段子类型 枚举
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum FormFieldSubTypeEnum implements BaseEnum<FormFieldSubTypeEnum> {

    /**
     * 字段子类型 input:0:普通文本 1:文本区域 select:0:单选 1:多选
     */
    INPUT_OR_RADIO(0, "普通文本或者单选"),
    TEXT_AREA_OR_MULTIPLE_SELECTION(1, "文本区域或者多选或图片"),
    FILE(1, "文件"),
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

}

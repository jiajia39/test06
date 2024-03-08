package com.framework.emt.system.domain.formfield.constant.enums;

import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型 枚举
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum FormFieldTypeEnum implements BaseEnum<FormFieldTypeEnum> {

    /**
     * 时间选择
     */
    INPUT(0, "文本输入框"),
    SELECT(1, "下拉选择框"),
    DATETIME(2, "日期时间选择框"),
    RADIO(3, "单选框"),
    CHECKBOX(4, "多选框"),
    SWITCH(5, "开关"),
    SLIDER(6, "滑块"),
    UPLOAD(7, "上传"),
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

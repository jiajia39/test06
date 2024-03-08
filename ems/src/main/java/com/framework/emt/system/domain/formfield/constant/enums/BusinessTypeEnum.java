package com.framework.emt.system.domain.formfield.constant.enums;

import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表单业务类型
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum implements BaseEnum<BusinessTypeEnum> {

    /**
     * 表单业务类型
     */
    EXCEPTION_SUBMIT(0, "异常提报"),
    EXCEPTION_RESPONSE(1, "异常响应"),
    EXCEPTION_HANDING(2, "异常处理"),
    EXCEPTION_SUSPENDING(3, "异常挂起"),
    EXCEPTION_COLLABORATION(4, "异常协同"),
    EXCEPTION_CHECK(5, "异常验收");

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

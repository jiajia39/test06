package com.framework.emt.system.infrastructure.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发送通道枚举
 *
 * @author yankunw
 * @since  2024-02-02
 */
@Getter
@AllArgsConstructor
public enum SendChannelEnum implements BaseEnum<SendChannelEnum> {

    /**
     * 禁用启用状态
     */
    UNI_PUSH(0, "uni_push"),
    WX_MP(1, "微信公众号"),
    WX_QX(2, "企业微信"),
    TT_PUSH(3, "钉钉"),;

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

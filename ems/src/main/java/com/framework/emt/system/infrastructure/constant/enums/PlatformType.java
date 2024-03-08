package com.framework.emt.system.infrastructure.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台类型 枚举
 * @author gaojia
 * @since 2024-02-21
 */
@Getter
@AllArgsConstructor
public enum PlatformType implements BaseEnum<PlatformType> {

    /**
     * 主状态-提报节点
     */
    WX_ENTERPRISE_APPLICATION(1, SendChannelEnum.WX_MP," 微信公众号"),
    WX_MINI_PROGRAM(2, SendChannelEnum.WX_MP,"微信小程序"),
    WX_OFFICIAL_ACCOUNT(3, SendChannelEnum.WX_QX,"微信企业应用"),
    DING_TALK(4, SendChannelEnum.TT_PUSH,"钉钉");

    /**
     * code编码
     */
    @EnumValue
    @JsonValue
    final Integer code;

    final SendChannelEnum channel;
    /**
     * 中文信息描述
     */
    final String name;


}

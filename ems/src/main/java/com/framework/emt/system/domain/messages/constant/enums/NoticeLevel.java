package com.framework.emt.system.domain.messages.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 通知级别 枚举
 *
 * @Author ds_C
 * @Date 2023-08-22
 */
@Getter
@AllArgsConstructor
public enum NoticeLevel implements BaseEnum<NoticeLevel> {

    /**
     * 通知级别
     */
    NORMAL(1, "普通"),
    OVERTIME_WARNING(2, "超时预警"),
    OVERTIME_REPORT(3, "超时上报");

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
     * 获取超时预警和超时上报的code编码
     *
     * @return
     */
    public static List<Integer> getCodes() {
        return Lists.newArrayList(OVERTIME_WARNING.getCode(), OVERTIME_REPORT.getCode());
    }

    /**
     * 校验通知级别code是否是：
     * 超时预警、超时上报
     *
     * @return
     */
    public static boolean validate(Integer code) {
        return OVERTIME_WARNING.getCode().equals(code) || OVERTIME_REPORT.getCode().equals(code);
    }

}

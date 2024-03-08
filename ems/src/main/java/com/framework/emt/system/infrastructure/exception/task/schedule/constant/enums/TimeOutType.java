package com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 超时类型 枚举
 *
 * @Author ds_C
 * @Date 2023-08-24
 */
@Getter
@AllArgsConstructor
public enum TimeOutType implements BaseEnum<TimeOutType> {

    /**
     * 超时类型
     */
    RESPONSE(0, "响应超时"),
    HANDING(1, "处理超时"),
    COOPERATION(2, "协同超时"),
    RESPONSE_REPORT(3, "响应逐级上报"),
    HANDING_REPORT(4, "处理逐级上报"),
    COOPERATION_REPORT(5, "协同逐级上报");

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
     * 获取响应相关的超时类型枚举
     *
     * @return
     */
    public static List<TimeOutType> getResponses() {
        return Lists.newArrayList(TimeOutType.RESPONSE, TimeOutType.RESPONSE_REPORT);
    }

    /**
     * 获取处理相关的超时类型枚举
     *
     * @return
     */
    public static List<TimeOutType> getHandings() {
        return Lists.newArrayList(TimeOutType.HANDING, TimeOutType.HANDING_REPORT);
    }

    /**
     * 获取协同相关的超时类型枚举
     *
     * @return
     */
    public static List<TimeOutType> getCooperations() {
        return Lists.newArrayList(TimeOutType.COOPERATION, TimeOutType.COOPERATION_REPORT);
    }

}

package com.framework.emt.system.domain.statistics.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.domain.tagexception.constant.enums.SourceTypeEnum;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 趋势信息 类型枚举
 *
 * @Author gaojia
 * @Date 2023-08-24
 */
@Getter
@AllArgsConstructor
public enum StatisticsType implements BaseEnum<StatisticsType> {

    /**
     * 异常类型
     */
    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    YEAR(4, "年");;

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
     * 统计类型
     *
     * @param key key
     * @return 统计类型
     */
    public static StatisticsType getEnum(Integer key) {
        for (StatisticsType statisticsType : values()) {
            if (ObjectUtil.equals(statisticsType.getCode(), key)) {
                return statisticsType;
            }
        }
        return null;
    }
}

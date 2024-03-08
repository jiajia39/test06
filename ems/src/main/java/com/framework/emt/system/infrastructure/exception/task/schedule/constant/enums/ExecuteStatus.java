package com.framework.emt.system.infrastructure.exception.task.schedule.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 执行状态 枚举
 *
 * @Author ds_C
 * @Date 2023-08-08
 */
@Getter
@AllArgsConstructor
public enum ExecuteStatus implements BaseEnum<ExecuteStatus> {

    /**
     * 执行状态
     */
    NOT(0, "未执行"),
    OVER(1, "已执行"),
    CANCEL(2, "已取消");

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
     * 根据执行状态code查询执行状态名称
     *
     * @param code 执行状态code
     * @return
     */
    public static String getNameByCode(Integer code) {
        return Stream.of(values())
                .filter(status -> status.getCode().equals(code))
                .map(ExecuteStatus::getName)
                .findFirst()
                .orElse(null);
    }

}

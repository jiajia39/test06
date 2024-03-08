package com.framework.emt.system.infrastructure.exception.task.handing.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常恢复方式
 *
 * @Author ds_C
 * @Date 2023-08-08
 */
@Getter
@AllArgsConstructor
public enum TaskResumeType implements BaseEnum<TaskResumeType> {

    /**
     * 异常恢复方式
     */
    INIT(0, "未恢复"),
    AUTO(1, "自动"),
    HAND(2, "手动");

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
     * 异常恢复方式
     *
     * @param code code
     * @return 异常恢复方式
     */
    public static TaskResumeType getEnum(Integer code) {
        for (TaskResumeType taskResumeType : values()) {
            if (ObjectUtil.equals(taskResumeType.getCode(), code)) {
                return taskResumeType;
            }
        }
        return null;
    }

}

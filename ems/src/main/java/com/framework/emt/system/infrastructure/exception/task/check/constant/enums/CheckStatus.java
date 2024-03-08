package com.framework.emt.system.infrastructure.exception.task.check.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验收子状态 枚举
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum CheckStatus implements BaseEnum<CheckStatus> {

    /**
     * 验收状态-验收节点
     */
    WAIT_CHECK(1, "待验收"),
    CHECK_REJECT(2, "验收驳回"),
    CHECK_PASSED(3, "验收通过");

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
     * 验收状态
     *
     * @param key key
     * @return 验收状态
     */
    public static CheckStatus getEnum(Integer key) {
        for (CheckStatus sourceTypeEnum : values()) {
            if (ObjectUtil.equals(sourceTypeEnum.getCode(), key)) {
                return sourceTypeEnum;
            }
        }
        return null;
    }
}

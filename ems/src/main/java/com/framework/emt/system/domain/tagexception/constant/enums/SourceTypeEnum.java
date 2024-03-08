package com.framework.emt.system.domain.tagexception.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签关联类型
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum SourceTypeEnum implements BaseEnum<SourceTypeEnum> {

    /**
     *  标签关联类型
     */
    KNOWLEDGE_BASE(0, "知识库"),
    ABNORMAL_PROCESS(1, "异常流程"),
    ABNORMAL_TASK(2, "异常任务");

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
     *  标签关联情况
     *
     * @param key key
     * @return  标签关联状态
     */
    public static SourceTypeEnum getEnum(Integer key) {
        for (SourceTypeEnum sourceTypeEnum : values()) {
            if (ObjectUtil.equals(sourceTypeEnum.getCode(), key)) {
                return sourceTypeEnum;
            }
        }
        return null;
    }
}

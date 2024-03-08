package com.framework.emt.system.domain.tag.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签类型
 *
 * @Author gaojia
 * @Date 2023-07-28
 */
@Getter
@AllArgsConstructor
public enum TagTypeEnum implements BaseEnum<TagTypeEnum> {

    /**
     * 业务类型
     */
    KNOWLEDGE_BASE_LABEL(0, "知识库标签"),
    ABNORMAL_CAUSE_ITEM(1, "异常原因项");


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

package com.framework.emt.system.infrastructure.exception.task.task.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 驳回节点 枚举
 *
 * @Author ds_C
 * @Date 2023-08-08
 */
@Getter
@AllArgsConstructor
public enum TaskRejectNode implements BaseEnum<TaskRejectNode> {

    /**
     * 驳回节点
     */
    INIT(0, "未驳回"),
    RESPONSE(1, "响应"),
    HANDING(2, "处理"),
    CHECK(3, "验收");

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

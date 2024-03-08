package com.framework.emt.system.domain.exception.convert.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionItemErrorCode;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 紧急程度 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum PriorityEnum implements BaseEnum<PriorityEnum> {

    /**
     * 紧急程度
     */
    SLIGHT(0, "轻微"),
    NORMAL(1, "一般"),
    EXIGENCY(2, "紧急"),
    EXTRA_URGENT(3, "特急");

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
     * 校验紧急程度名称列表中的紧急程度都存在
     *
     * @param priorityNameList 紧急程度名称列表
     */
    public static void checkPriorityNameList(List<String> priorityNameList) {
        List<String> priorityEnumNameList = Stream.of(PriorityEnum.values()).map(PriorityEnum::getName)
                .collect(Collectors.toList());
        if (!priorityEnumNameList.containsAll(priorityNameList)) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_ITEM_PRIORITY);
        }
    }

}

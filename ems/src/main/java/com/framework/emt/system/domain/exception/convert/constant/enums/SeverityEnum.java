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
 * 严重程度 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum SeverityEnum implements BaseEnum<SeverityEnum> {

    /**
     * 严重程度
     */
    SLIGHT(0, "轻微"),
    NORMAL(1, "一般"),
    SERIOUSNESS(2, "严重"),
    DEADLY(3, "致命");

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
     * 校验严重程度名称列表中的严重程度都存在
     *
     * @param severityNameList 严重程度名称列表
     */
    public static void checkSeverityNameList(List<String> severityNameList) {
        List<String> severityEnumNameList = Stream.of(SeverityEnum.values()).map(SeverityEnum::getName)
                .collect(Collectors.toList());
        if (!severityEnumNameList.containsAll(severityNameList)) {
            throw new ServiceException(ExceptionItemErrorCode.EXCEl_CONTAIN_NOT_EXIST_EXCEPTION_ITEM_SEVERITY);
        }
    }

}

package com.framework.emt.system.infrastructure.utils;

import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import com.framework.emt.system.infrastructure.common.response.EnumResponse;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举字典工具类
 *
 * @author jiaXue
 * date 2023/4/12
 */
@SuppressWarnings("unused")
public class EnumDictUtils {

    private EnumDictUtils() {
    }

    /**
     * 枚举字典转成列表
     *
     * @param cls 枚举类
     * @param <T> 泛型
     * @return 字典列表
     */
    public static <T extends Enum<T> & BaseEnum<T>> List<EnumResponse> getEnumDictVo(Class<T> cls) {
        return EnumSet.allOf(cls).stream().map(et -> {
                    EnumResponse vo = new EnumResponse();
                    vo.setCode(et.getCode());
                    vo.setName(et.getName());
                    vo.setText(et.name());
                    return vo;
                }
        ).collect(Collectors.toList());
    }

    /**
     * 枚举字典转成列表（可以根据em过滤掉枚举中的枚举常量）
     * @param cls 枚举类
     * @param em 要被过滤掉的枚举常量
     * @param <T> 泛型
     * @return 字典列表
     */
    public static <T extends Enum<T> & BaseEnum<T>> List<EnumResponse> getEnumDictVo(Class<T> cls, Enum<T> em) {
        return EnumSet.allOf(cls).stream()
                .filter(et -> et != em)
                .map(et -> {
                    EnumResponse vo = new EnumResponse();
                    vo.setCode(et.getCode());
                    vo.setName(et.getName());
                    vo.setText(et.name());
                    return vo;
                }).collect(Collectors.toList());
    }

}

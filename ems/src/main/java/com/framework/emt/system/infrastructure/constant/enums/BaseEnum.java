package com.framework.emt.system.infrastructure.constant.enums;

/**
 * 基础枚举
 *
 * @author jiaXue
 * date 2023/3/14
 */
public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    Integer getCode();

    /**
     * 获取编码名称，便于维护
     *
     * @return 获取编码名称
     */
    String getName();

    /**
     * 根据code码获取枚举
     *
     * @param cls  enum class
     * @param code enum code
     * @return get enum
     */
    static <T extends Enum<T> & BaseEnum<T>> T parseByCode(Class<T> cls, Integer code) {
        if (code == null) {
            return null;
        }
        for (T t : cls.getEnumConstants()) {
            if (t.getCode().intValue() == code.intValue()) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据code码获取枚举
     *
     * @param cls  enum class
     * @param name enum name
     * @return get enum
     */
    static <T extends Enum<T> & BaseEnum<T>> T parseByName(Class<T> cls, String name) {
        for (T t : cls.getEnumConstants()) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

}

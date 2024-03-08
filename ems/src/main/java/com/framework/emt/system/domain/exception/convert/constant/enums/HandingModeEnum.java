package com.framework.emt.system.domain.exception.convert.constant.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionProcessErrorCode;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 处理模式 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum HandingModeEnum implements BaseEnum<HandingModeEnum> {

    /**
     * 处理模式
     */
    NOT_SPECIFIED(0, "不指定"),
    FIXED_PERSONNEL(1, "固定人员"),
    MULTIPLE_PERSONNEL(2, "多个人员"),
    RESPONSE_AND_HANDING(3, "响应同处理");

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
     * 校验处理人数量是否符合处理模式的要求
     *
     * @param modeCode 处理模式Code
     * @param userNum  处理人数量
     */
    public static void checkUserNumByModeCode(Integer modeCode, Integer userNum) {
        if (HandingModeEnum.NOT_SPECIFIED.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.HANDING_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE);
            }
        }
        if (HandingModeEnum.FIXED_PERSONNEL.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ONE.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.HANDING_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE);
            }
        }
        if (HandingModeEnum.MULTIPLE_PERSONNEL.getCode().equals(modeCode)) {
            if (NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.HANDING_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE);
            }
        }
        if (HandingModeEnum.RESPONSE_AND_HANDING.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.HANDING_MODE_IS_RESPONSE_AND_HANDING_USER_NOT_HAVE);
            }
        }
    }

    /**
     * 处理模式
     *
     * @param key key
     * @return 处理模式
     */
    public static HandingModeEnum getEnum(Integer key) {
        for (HandingModeEnum handingModeEnum : values()) {
            if (ObjectUtil.equals(handingModeEnum.getCode(), key)) {
                return handingModeEnum;
            }
        }
        return null;
    }

}
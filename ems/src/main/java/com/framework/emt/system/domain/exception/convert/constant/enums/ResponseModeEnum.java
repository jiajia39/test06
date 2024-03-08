package com.framework.emt.system.domain.exception.convert.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.framework.common.api.exception.ServiceException;
import com.framework.emt.system.domain.exception.convert.constant.code.ExceptionProcessErrorCode;
import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 响应模式 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum ResponseModeEnum implements BaseEnum<ResponseModeEnum> {

    /**
     * 响应模式
     */
    NOT_SPECIFIED(0, "不指定"),
    FIXED_PERSONNEL(1, "固定人员"),
    MULTIPLE_PERSONNEL(2, "多个人员");

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
     * 校验响应人数量是否符合响应模式的要求
     *
     * @param modeCode 响应模式Code
     * @param userNum  响应人数量
     */
    public static void checkUserNumByModeCode(Integer modeCode, Integer userNum) {
        if (ResponseModeEnum.NOT_SPECIFIED.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.RESPONSE_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE);
            }
        }
        if (ResponseModeEnum.FIXED_PERSONNEL.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ONE.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.RESPONSE_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE);
            }
        }
        if (ResponseModeEnum.MULTIPLE_PERSONNEL.getCode().equals(modeCode)) {
            if (NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.RESPONSE_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE);
            }
        }
    }

}

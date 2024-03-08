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
 * 验收模式 枚举
 *
 * @Author ds_C
 * @Date 2023-07-12
 */
@Getter
@AllArgsConstructor
public enum CheckModeEnum implements BaseEnum<CheckModeEnum> {

    /**
     * 验收模式
     */
    NOT_SPECIFIED(0, "不指定"),
    FIXED_PERSONNEL(1, "固定人员"),
    MULTIPLE_PERSONNEL(2, "多个人员"),
    REPORT_AND_CHECK(3, "填报同验收"),
    REPORT_AND_CHECK_MULTIPLE_PEOPLE(4, "填报同验收多人");

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
     * 校验验收人数量是否符合验收模式的要求
     *
     * @param modeCode 验收模式Code
     * @param userNum  验收人数量
     */
    public static void checkUserNumByModeCode(Integer modeCode, Integer userNum) {
        if (CheckModeEnum.NOT_SPECIFIED.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.CHECK_MODE_IS_NOT_SPECIFIED_USER_NOT_HAVE);
            }
        }
        if (CheckModeEnum.FIXED_PERSONNEL.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ONE.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.CHECK_MODE_IS_FIXED_PERSONNEL_USER_HAVE_ONLY_ONE);
            }
        }
        if (CheckModeEnum.MULTIPLE_PERSONNEL.getCode().equals(modeCode)) {
            if (NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.CHECK_MODE_IS_MULTIPLE_PERSONNEL_USER_HAVE_MULTIPLE);
            }
        }
        if (CheckModeEnum.REPORT_AND_CHECK.getCode().equals(modeCode)) {
            if (!NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.CHECK_MODE_IS_REPORT_AND_CHECK_USER_NOT_HAVE);
            }
        }
        if (CheckModeEnum.REPORT_AND_CHECK_MULTIPLE_PEOPLE.getCode().equals(modeCode)) {
            if (NumberUtils.INTEGER_ZERO.equals(userNum)) {
                throw new ServiceException(ExceptionProcessErrorCode.CHECK_MODE_IS_REPORT_AND_CHECK_MULTIPLE_PEOPLE_USER_HAVE_MULTIPLE);
            }
        }
    }

    /**
     * 验收模式
     *
     * @param key key
     * @return 验收模式
     */
    public static CheckModeEnum getEnum(Integer key) {
        for (CheckModeEnum checkModeEnum : values()) {
            if (ObjectUtil.equals(checkModeEnum.getCode(), key)) {
                return checkModeEnum;
            }
        }
        return null;
    }

}

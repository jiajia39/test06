package com.framework.emt.system.infrastructure.common.validation;

import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 枚举通用注解校验类
 *
 * @author jiaXue
 * date 2023/3/20
 */
public class EnumConstraintValidator implements ConstraintValidator<EnumValidator, Integer> {

    private BaseEnum<?>[] enumConstants;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        enumConstants = constraintAnnotation.enumClazz().getEnumConstants();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(integer)) {
            return Boolean.TRUE;
        }
        for (BaseEnum<?> iIterateEnum : enumConstants) {
            if (iIterateEnum.getCode().equals(integer)) {
                return true;
            }
        }
        return false;
    }
}

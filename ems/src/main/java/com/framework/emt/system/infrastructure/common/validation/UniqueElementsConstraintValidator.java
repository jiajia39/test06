package com.framework.emt.system.infrastructure.common.validation;

import cn.hutool.core.collection.CollectionUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

/**
 * ID列表不能有重复值注解校验类
 *
 * @author ds_C
 * @since 2023-07-29
 */
public class UniqueElementsConstraintValidator implements ConstraintValidator<UniqueElementsValidator, List<Long>> {

    @Override
    public void initialize(UniqueElementsValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Long> idList, ConstraintValidatorContext context) {
        return CollectionUtil.isEmpty(idList) || new HashSet<>(idList).size() == idList.size();
    }

}

package com.framework.emt.system.infrastructure.common.validation;

import com.framework.emt.system.infrastructure.constant.enums.BaseEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举通用注解
 *
 * @author jiaXue
 * date 2023/3/20
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = EnumConstraintValidator.class)
public @interface EnumValidator {

    String message() default "枚举值不在范围内";

    Class<? extends BaseEnum<?>> enumClazz();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

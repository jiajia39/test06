package com.framework.emt.system.infrastructure.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验ID列表不能有重复值注解
 *
 * @author ds_C
 * @since 2023-07-29
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = UniqueElementsConstraintValidator.class)
public @interface UniqueElementsValidator {

    String message() default "列表不能有重复值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

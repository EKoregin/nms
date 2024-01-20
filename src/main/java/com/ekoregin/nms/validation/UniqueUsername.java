package com.ekoregin.nms.validation;

import com.ekoregin.nms.validation.impl.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

    String message() default "Пользователь с таким именем уже зарегистрирован";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}

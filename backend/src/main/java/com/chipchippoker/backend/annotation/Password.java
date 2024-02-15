package com.chipchippoker.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chipchippoker.backend.annotation.validator.PasswordValidator;

import jakarta.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
	String message() default "유효하지 않은 형식의 비밀번호입니다.";

	Class[] groups() default {};

	Class[] payload() default {};
}

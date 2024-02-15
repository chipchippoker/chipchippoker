package com.chipchippoker.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chipchippoker.backend.annotation.validator.MemberIdValidator;

import jakarta.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MemberIdValidator.class)
public @interface MemberId {
	String message() default "유효하지 않은 형식의 아이디입니다.";

	Class[] groups() default {};

	Class[] payload() default {};
}

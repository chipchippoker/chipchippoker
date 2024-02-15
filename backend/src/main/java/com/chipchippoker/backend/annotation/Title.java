package com.chipchippoker.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chipchippoker.backend.annotation.validator.TitleValidator;

import jakarta.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TitleValidator.class)
public @interface Title {
	String message() default "유효하지 않은 형식의 제목입니다.";

	Class[] groups() default {};

	Class[] payload() default {};
}
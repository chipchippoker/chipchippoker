package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.Title;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<Title, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) { // null, 빈 문자열, 공백 불가
			return false;
		}
		return value.matches("^.{1,20}$"); // 1~20자 제한
	}
}

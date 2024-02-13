package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.Nickname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) {
			return false;
		}
		return value.matches("^[a-zA-Z가-힣0-9_]{2,12}$");
	}
}

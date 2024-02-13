package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.MemberId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberIdValidator implements ConstraintValidator<MemberId, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) {
			return false;
		}
		return value.matches("^[a-zA-Z0-9]{6,12}$");
	}
}

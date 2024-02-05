package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.MemberId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberIdValidator implements ConstraintValidator<MemberId, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) { // null, 빈 문자열, 공백 불가
			return false;
		}
		return value.matches("^[a-zA-Z0-9]{6,12}$"); // 영어 혹은 숫자 조합, 6~12자 제한
	}
}

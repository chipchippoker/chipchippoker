package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) { // null, 빈 문자열, 공백 불가
			return false;
		}
		return value.matches(
			"^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+}{\":?><;.,'`~])[a-zA-Z0-9!@#$%^&*()_+}{\":?><;.,'`~]{8,30}$"); // 영어와 숫자와 특수문자 혼합된 조합, 8~30자 제한
	}
}

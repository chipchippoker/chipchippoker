package com.chipchippoker.backend.annotation.validator;

import com.chipchippoker.backend.annotation.Nickname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value.isBlank()) { // null, 빈 문자열, 공백 불가
			return false;
		}
		return value.matches("^[a-zA-Z가-힣0-9_]{2,12}$"); // 한글 혹은 영어 혹은 숫자 혹은 특수문자(_) 조합, 2~12자 제한
	}
}

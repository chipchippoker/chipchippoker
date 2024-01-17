package com.chipchippoker.backend.api.auth.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberSocialType{

	KAKAO("KAKAO"),
	;
	private final String description;

}

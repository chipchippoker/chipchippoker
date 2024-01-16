package com.chipchippoker.backend.api.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
	private String memberId;
	private String password;
	private String passwordConfirm;
	private String nickname;
	private String icon;
}

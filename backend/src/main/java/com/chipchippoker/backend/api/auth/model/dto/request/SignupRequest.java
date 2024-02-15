package com.chipchippoker.backend.api.auth.model.dto.request;

import com.chipchippoker.backend.annotation.MemberId;
import com.chipchippoker.backend.annotation.Nickname;
import com.chipchippoker.backend.annotation.Password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
	@MemberId
	private String memberId;
	@Password
	private String password;
	private String passwordConfirm;
	@Nickname
	private String nickname;
	private String icon;
}

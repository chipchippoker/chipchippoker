package com.chipchippoker.backend.api.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSignupRequest {


	private String nickname;
	private String icon;
}
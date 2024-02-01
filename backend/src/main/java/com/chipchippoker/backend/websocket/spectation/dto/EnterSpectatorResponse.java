package com.chipchippoker.backend.websocket.spectation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterSpectatorResponse {
	private String nickname;

	public static EnterSpectatorResponse create(String nickname) {
		return EnterSpectatorResponse.builder()
			.nickname(nickname)
			.build();
	}
}

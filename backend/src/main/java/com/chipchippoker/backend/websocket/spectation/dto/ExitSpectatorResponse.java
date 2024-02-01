package com.chipchippoker.backend.websocket.spectation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitSpectatorResponse {
	private String nickname;

	public static ExitSpectatorResponse create(String nickname) {
		return ExitSpectatorResponse.builder()
			.nickname(nickname)
			.build();
	}
}

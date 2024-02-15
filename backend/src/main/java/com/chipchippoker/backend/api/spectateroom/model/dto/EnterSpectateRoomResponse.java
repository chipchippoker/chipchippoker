package com.chipchippoker.backend.api.spectateroom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnterSpectateRoomResponse {
	private Long roomId;
	private String title;
	private String state;

	public static EnterSpectateRoomResponse enterSpectateRoomResponse(Long roomId, String title, String state) {
		return EnterSpectateRoomResponse.builder()
			.roomId(roomId)
			.title(title)
			.state(state)
			.build();
	}
}

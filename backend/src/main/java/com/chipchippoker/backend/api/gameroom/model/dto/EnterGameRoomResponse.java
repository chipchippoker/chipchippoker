package com.chipchippoker.backend.api.gameroom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnterGameRoomResponse {
	private Long roomId;

	public static EnterGameRoomResponse enterGameRoomResponse(Long roomId) {
		return EnterGameRoomResponse.builder()
			.roomId(roomId)
			.build();
	}
}

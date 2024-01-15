package com.chipchippoker.backend.api.gameroom.model.dto;

import com.chipchippoker.backend.common.entity.GameRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateGameRoomResponse {
	private Long roomId;
	private String title;
	private Integer totalParticipantCnt;

	public static CreateGameRoomResponse createGameRoomResponse(GameRoom gameRoom) {
		return CreateGameRoomResponse.builder()
			.roomId(gameRoom.getId())
			.title(gameRoom.getTitle())
			.totalParticipantCnt(gameRoom.getTotalParticipantCnt())
			.build();
	}
}

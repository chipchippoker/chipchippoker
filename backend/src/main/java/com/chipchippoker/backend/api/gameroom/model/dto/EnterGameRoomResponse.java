package com.chipchippoker.backend.api.gameroom.model.dto;

import com.chipchippoker.backend.common.entity.GameRoom;

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
	private String title;
	private Integer totalParticipantCnt;

	public static EnterGameRoomResponse enterGameRoomResponse(GameRoom gameRoom) {
		return EnterGameRoomResponse.builder()
			.roomId(gameRoom.getId())
			.title(gameRoom.getTitle())
			.totalParticipantCnt(gameRoom.getTotalParticipantCnt())
			.build();
	}
}

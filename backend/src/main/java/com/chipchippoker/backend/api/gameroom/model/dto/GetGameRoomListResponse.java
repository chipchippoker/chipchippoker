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
public class GetGameRoomListResponse {
	private Boolean isPrivate;
	private String state;
	private String title;
	private Integer totalParticipantCnt;
	private Integer currentParticipantCnt;
	private Integer currentSpectatorCnt;

	public static GetGameRoomListResponse gameRoomListResponse(GameRoom gameRoom) {
		return GetGameRoomListResponse.builder()
			.isPrivate(gameRoom.getIsPrivate())
			.state(gameRoom.getState())
			.title(gameRoom.getTitle())
			.totalParticipantCnt(gameRoom.getTotalParticipantCnt())
			.currentParticipantCnt(gameRoom.getMembers().size())
			.currentSpectatorCnt(gameRoom.getSpectateRoom().getMembers().size())
			.build();
	}
}

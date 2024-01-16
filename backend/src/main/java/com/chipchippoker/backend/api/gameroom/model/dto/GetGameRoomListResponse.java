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
	private Integer totalParticipantsCnt;
	private Integer currentParticipantsCnt;
	private Integer currentSpectatorsCnt;

	public static GetGameRoomListResponse gameRoomListResponse(GameRoom gameRoom) {
		return GetGameRoomListResponse.builder()
			.isPrivate(gameRoom.getIsPrivate())
			.state(gameRoom.getState())
			.title(gameRoom.getTitle())
			.totalParticipantsCnt(gameRoom.getTotalParticipantCnt())
			.currentParticipantsCnt(gameRoom.getMembers().size())
			.currentSpectatorsCnt(gameRoom.getSpectateRoom().getMembers().size())
			.build();
	}
}

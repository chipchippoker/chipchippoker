package com.chipchippoker.backend.api.gameroom.model.dto;

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

	public static GetGameRoomListResponse gameRoomListResponse(Boolean isPrivate, String state, String title,
		Integer totalParticipantCnt, Integer currentParticipantCnt, Integer currentSpectatorCnt) {
		return GetGameRoomListResponse.builder()
			.isPrivate(isPrivate)
			.state(state)
			.title(title)
			.totalParticipantCnt(totalParticipantCnt)
			.currentParticipantCnt(currentParticipantCnt)
			.currentSpectatorCnt(currentSpectatorCnt)
			.build();
	}
}

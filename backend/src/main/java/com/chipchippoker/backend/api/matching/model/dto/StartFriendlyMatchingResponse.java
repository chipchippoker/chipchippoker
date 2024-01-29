package com.chipchippoker.backend.api.matching.model.dto;

import com.chipchippoker.backend.common.entity.GameRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartFriendlyMatchingResponse {
	private Long roomId;
	private String title;
	private Integer totalParticipantCnt;
	private Boolean isFirstParticipant;

	public static StartFriendlyMatchingResponse startFriendlyMatchingResponse(GameRoom gameRoom,
		Boolean isFirstParticipant) {
		return StartFriendlyMatchingResponse.builder()
			.roomId(gameRoom.getId())
			.title(gameRoom.getTitle())
			.totalParticipantCnt(gameRoom.getTotalParticipantCnt())
			.isFirstParticipant(isFirstParticipant)
			.build();
	}
}

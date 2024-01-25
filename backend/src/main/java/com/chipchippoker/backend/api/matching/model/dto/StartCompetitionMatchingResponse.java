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
public class StartCompetitionMatchingResponse {
	private Long roomId;
	private String title;
	private Integer totalParticipantCnt;

	public static StartCompetitionMatchingResponse startCompetitionMatchingResponse(GameRoom gameRoom) {
		return StartCompetitionMatchingResponse.builder()
			.roomId(gameRoom.getId())
			.title(gameRoom.getTitle())
			.totalParticipantCnt(gameRoom.getTotalParticipantCnt())
			.build();
	}
}
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
public class QuitMatchingResponse {
	private String title;

	public static QuitMatchingResponse quitMatchingResponse(GameRoom gameRoom) {
		return QuitMatchingResponse.builder()
			.title(gameRoom.getTitle())
			.build();
	}
}

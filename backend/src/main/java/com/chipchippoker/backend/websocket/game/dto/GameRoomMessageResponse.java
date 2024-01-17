package com.chipchippoker.backend.websocket.game.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.GameMemberInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomMessageResponse {
	private Integer currentRound;
	private List<GameMemberInfo> gameMemberInfos = new ArrayList<>();

	public static GameRoomMessageResponse create(GameManager gameManager) {
		return GameRoomMessageResponse.builder()
			.currentRound(gameManager.getCurrentRound())
			.gameMemberInfos(gameManager.getMemberManagerMap()
				.values()
				.stream()
				.map(GameMemberInfo::create)
				.collect(Collectors.toList()))
			.build();
	}
}

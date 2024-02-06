package com.chipchippoker.backend.websocket.game.dto;

import java.util.ArrayList;
import java.util.List;

import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberInfo;
import com.chipchippoker.backend.websocket.game.model.MemberManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadyRoomMessageResponse {
	private Integer countOfPeople;
	private List<MemberInfo> memberInfos = new ArrayList<>();
	private String roomManagerNickname;

	public static ReadyRoomMessageResponse create(GameManager gameManager) {

		return ReadyRoomMessageResponse.builder()
			.countOfPeople(gameManager.getCountOfPeople())
			.memberInfos(gameManager.getMemberManagersByCreatedAt().stream().map(MemberManager::getMemberInfo).toList())
			.roomManagerNickname(gameManager.getRoomManager())
			.build();
	}
}

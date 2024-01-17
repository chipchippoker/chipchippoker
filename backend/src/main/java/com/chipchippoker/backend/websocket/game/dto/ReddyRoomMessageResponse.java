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
public class ReddyRoomMessageResponse {
	private Integer countOfPeople;
	private List<MemberInfo> memberInfos = new ArrayList<>();
	private String roomManagerNickname;

	public static ReddyRoomMessageResponse create(GameManager gameManager) {
		return ReddyRoomMessageResponse.builder()
			.countOfPeople(gameManager.getCountOfPeople())
			.memberInfos(gameManager.getMemberManagerMap().values().stream().map(MemberManager::getMemberInfo).toList())
			.roomManagerNickname(gameManager.getRoomManager())
			.build();
	}
}

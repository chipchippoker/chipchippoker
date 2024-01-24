package com.chipchippoker.backend.websocket.matching.dto;

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
public class CompleteMatchingMessageResponse {
	private Integer countOfPeople;
	private List<MemberInfo> memberInfos = new ArrayList<>();

	public static CompleteMatchingMessageResponse create(GameManager gameManager) {
		return CompleteMatchingMessageResponse.builder()
			.countOfPeople(gameManager.getCountOfPeople())
			.memberInfos(gameManager.getMemberManagerMap().values().stream().map(MemberManager::getMemberInfo).toList())
			.build();
	}
}

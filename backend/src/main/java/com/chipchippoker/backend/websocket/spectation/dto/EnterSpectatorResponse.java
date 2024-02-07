package com.chipchippoker.backend.websocket.spectation.dto;

import java.util.ArrayList;
import java.util.List;

import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.MemberInfo;
import com.chipchippoker.backend.websocket.game.model.MemberManager;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterSpectatorResponse {

	private List<MemberInfo> memberInfos = new ArrayList<>();
	private List<String> spectatorList = new ArrayList<>();

	public static EnterSpectatorResponse create(GameManager gameManager, SpectationManager spectationManager) {
		return EnterSpectatorResponse.builder()
			.memberInfos(gameManager.getMemberManagersByCreatedAt().stream().map(MemberManager::getMemberInfo).toList())
			.spectatorList(spectationManager.getSpectatorList())
			.build();
	}
}

package com.chipchippoker.backend.websocket.spectation.dto;

import java.util.ArrayList;
import java.util.List;

import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.game.model.GameMemberInfo;
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
public class GameRoomForSpectatorMessageResponse {
	private Boolean roundState;
	private Integer currentRound;
	private String yourTurn;
	private List<GameMemberInfo> gameMemberInfos = new ArrayList<>();
	private List<String> spectators = new ArrayList<>();

	public static GameRoomForSpectatorMessageResponse getCurrentState(GameManager gameManager,
		SpectationManager spectationManager,
		ArrayList<MemberManager> memberManagers) {
		ArrayList<GameMemberInfo> result = new ArrayList<>();
		for (MemberManager memberManager : memberManagers) {
			result.add(new GameMemberInfo(memberManager.getMemberInfo().getNickname()
				, memberManager.getMemberGameInfo().getHaveCoin(),
				memberManager.getMemberGameInfo().getBettingCoin(),
				memberManager.getMemberGameInfo().getCardInfo(),
				memberManager.getMemberGameInfo().getIsState()));
		}
		return GameRoomForSpectatorMessageResponse.builder()
			.roundState(Boolean.TRUE)
			.currentRound(gameManager.getCurrentRound())
			.yourTurn(gameManager.getOrder().get(gameManager.getTurnNumber()))
			.gameMemberInfos(result)
			.spectators(spectationManager.getSpectatorList())
			.build();
	}
}

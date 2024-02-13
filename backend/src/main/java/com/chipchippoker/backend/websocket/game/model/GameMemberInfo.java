package com.chipchippoker.backend.websocket.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameMemberInfo {
	private String nickname;
	private Integer haveCoin;
	private Integer bettingCoin;
	private CardInfo cardInfo;
	private String isState;

	public static ArrayList<GameMemberInfo> createListInRoundProceed(
		GameManager gameManager,
		ArrayList<MemberManager> isNotTurnMemberManagers,
		MemberManager isTurnMemberManager) {
		HashMap<String, GameMemberInfo> temp = new HashMap<>();
		for (MemberManager isNotTurnMemberManager : isNotTurnMemberManagers) {
			temp.put(isNotTurnMemberManager.getMemberInfo().getNickname(),
				new GameMemberInfo(isNotTurnMemberManager.getMemberInfo().getNickname()
					, isNotTurnMemberManager.getMemberGameInfo().getHaveCoin(),
					isNotTurnMemberManager.getMemberGameInfo().getBettingCoin(),
					isNotTurnMemberManager.getMemberGameInfo().getCardInfo(),
					isNotTurnMemberManager.getMemberGameInfo().getIsState()));
		}

		temp.put(isTurnMemberManager.getMemberInfo().getNickname(),
			new GameMemberInfo(isTurnMemberManager.getMemberInfo().getNickname(),
				isTurnMemberManager.getMemberGameInfo().getHaveCoin(),
				isTurnMemberManager.getMemberGameInfo().getBettingCoin(),
				new CardInfo(0, 0),
				isTurnMemberManager.getMemberGameInfo().getIsState()));

		ArrayList<GameMemberInfo> result = new ArrayList<>();

		for (MemberManager manager : gameManager.getMemberManagersByCreatedAt()) {
			result.add(temp.get(manager.getMemberInfo().getNickname()));
		}

		return result;
	}

	public static ArrayList<GameMemberInfo> createListRoundEnd(GameManager gameManager,
		Collection<MemberManager> memberManagers) {
		HashMap<String, GameMemberInfo> temp = new HashMap<>();
		for (MemberManager memberManager : memberManagers) {
			temp.put(memberManager.getMemberInfo().getNickname(),
				new GameMemberInfo(memberManager.getMemberInfo().getNickname(),
					memberManager.getMemberGameInfo().getHaveCoin(),
					memberManager.getMemberGameInfo().getBettingCoin(),
					memberManager.getMemberGameInfo().getCardInfo(),
					memberManager.getMemberGameInfo().getIsState()));
		}

		ArrayList<GameMemberInfo> result = new ArrayList<>();

		for (MemberManager manager : gameManager.getMemberManagersByCreatedAt()) {
			result.add(temp.get(manager.getMemberInfo().getNickname()));
		}

		return result;
	}
}

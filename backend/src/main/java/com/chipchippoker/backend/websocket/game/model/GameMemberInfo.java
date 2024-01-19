package com.chipchippoker.backend.websocket.game.model;

import java.util.ArrayList;
import java.util.Collection;

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
	private Integer cardNumber;

	public static ArrayList<GameMemberInfo> createListInRoundProceed(ArrayList<MemberManager> isNotTurnMemberManagers,
		MemberManager isTurnMemberManager) {
		ArrayList<GameMemberInfo> result = new ArrayList<>();
		for (MemberManager isNotTurnMemberManager : isNotTurnMemberManagers) {
			result.add(new GameMemberInfo(isNotTurnMemberManager.getMemberInfo().getNickname()
				, isNotTurnMemberManager.getMemberGameInfo().getHaveCoin(),
				isNotTurnMemberManager.getMemberGameInfo().getBettingCoin(),
				isNotTurnMemberManager.getMemberGameInfo().getCardNumber()));
		}

		result.add(new GameMemberInfo(isTurnMemberManager.getMemberInfo().getNickname(),
			isTurnMemberManager.getMemberGameInfo().getHaveCoin(),
			isTurnMemberManager.getMemberGameInfo().getBettingCoin(),
			0));

		return result;
	}

	public static ArrayList<GameMemberInfo> createListRoundEnd(Collection<MemberManager> memberManagers) {
		ArrayList<GameMemberInfo> result = new ArrayList<>();
		for (MemberManager memberManager : memberManagers) {
			result.add(new GameMemberInfo(memberManager.getMemberInfo().getNickname(),
				memberManager.getMemberGameInfo().getHaveCoin(),
				memberManager.getMemberGameInfo().getBettingCoin(),
				memberManager.getMemberGameInfo().getCardNumber()));
		}

		return result;
	}

	public static GameMemberInfo create(MemberManager memberManager) {
		return GameMemberInfo.builder()
			.nickname(memberManager.getMemberInfo().getNickname())
			.haveCoin(memberManager.getMemberGameInfo().getHaveCoin())
			.bettingCoin(memberManager.getMemberGameInfo().getBettingCoin())
			.cardNumber(memberManager.getMemberGameInfo().getCardNumber())
			.build();
	}
}

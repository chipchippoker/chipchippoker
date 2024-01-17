package com.chipchippoker.backend.websocket.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameMemberInfo {
	private Integer haveCoin;
	private Integer bettingCoin;
	private Integer cardNumber;

	public static GameMemberInfo create(MemberManager memberManager) {
		return GameMemberInfo.builder()
			.haveCoin(memberManager.getHaveCoin())
			.bettingCoin(memberManager.getBettingCoin())
			.cardNumber(memberManager.getCardNumber())
			.build();
	}
}

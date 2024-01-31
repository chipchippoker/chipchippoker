package com.chipchippoker.backend.websocket.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberGameInfo {
	private CardInfo cardInfo;
	private Integer haveCoin;
	private Integer bettingCoin;
	private String isState;

	public static MemberGameInfo create() {
		return MemberGameInfo.builder()
			.cardInfo(new CardInfo(0, 0))
			.haveCoin(25)
			.bettingCoin(0)
			.isState("BET")
			.build();
	}
}

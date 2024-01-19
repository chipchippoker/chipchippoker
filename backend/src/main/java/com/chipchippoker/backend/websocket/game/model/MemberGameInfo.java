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
	private Integer cardNumber;
	private Integer haveCoin;
	private Integer bettingCoin;
	private String isState;

	public static MemberGameInfo create() {
		return MemberGameInfo.builder()
			.cardNumber(0)
			.haveCoin(25)
			.bettingCoin(0)
			.isState("BET")
			.build();
	}
}

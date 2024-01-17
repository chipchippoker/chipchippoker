package com.chipchippoker.backend.websocket.game.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class RoundManager {
	private final Integer round;
	private Integer canMaxBetCoin;
	private Integer canMinBetCoin;

	public RoundManager(Integer round) {
		this.round = round;
	}
}

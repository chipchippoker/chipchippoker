package com.chipchippoker.backend.websocket.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingMessage {
	/*
	DIE, FOLD, CALL, RUN
	 */
	private Integer currentRound;
	private String action;
	private Integer bettingCoin;
}

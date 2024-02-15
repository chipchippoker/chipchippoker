package com.chipchippoker.backend.websocket.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRoomMessageRequest {
	private Integer countOfPeople;
}

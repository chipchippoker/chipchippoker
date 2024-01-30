package com.chipchippoker.backend.websocket.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMatchingMessageRequest {
	private Integer countOfPeople;
}

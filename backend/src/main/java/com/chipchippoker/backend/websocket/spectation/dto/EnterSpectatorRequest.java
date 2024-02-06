package com.chipchippoker.backend.websocket.spectation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterSpectatorRequest {
	String gameState;
}

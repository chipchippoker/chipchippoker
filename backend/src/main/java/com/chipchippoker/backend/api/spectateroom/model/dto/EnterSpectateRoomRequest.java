package com.chipchippoker.backend.api.spectateroom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnterSpectateRoomRequest {
	private String title;
	private Integer password;
}

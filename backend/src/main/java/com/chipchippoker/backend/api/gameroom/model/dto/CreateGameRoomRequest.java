package com.chipchippoker.backend.api.gameroom.model.dto;

import com.chipchippoker.backend.annotation.Title;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRoomRequest {
	@Title
	private String title;
	private Boolean isPrivate;
	private Integer password;
	private Integer totalParticipantCnt;
}

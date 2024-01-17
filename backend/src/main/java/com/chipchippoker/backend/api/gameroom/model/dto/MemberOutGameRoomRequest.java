package com.chipchippoker.backend.api.gameroom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberOutGameRoomRequest {
	private String title;
	private String nickname;
}

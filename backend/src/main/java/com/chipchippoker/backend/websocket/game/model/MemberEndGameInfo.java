package com.chipchippoker.backend.websocket.game.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEndGameInfo {
	private String nickname;
	private String isResult;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer pointChange;
}

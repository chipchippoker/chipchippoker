package com.chipchippoker.backend.websocket.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfo {
	private String nickname;
	private Integer win;
	private Integer draw;
	private Integer lose;
	private Double rate;
	private Boolean isReady;
	private Boolean isRoomManager;

	public static MemberInfo create(String nickname, Boolean isRoomManager) {
		return MemberInfo.builder()
			.nickname(nickname)
			.win(0)
			.draw(0)
			.lose(0)
			.rate(0.0)
			.isReady(Boolean.FALSE)
			.isRoomManager(isRoomManager)
			.build();
	}
}

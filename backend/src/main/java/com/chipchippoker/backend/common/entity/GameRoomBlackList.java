package com.chipchippoker.backend.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRoomBlackList extends BaseEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id")
	private GameRoom gameRoom;

	public static GameRoomBlackList createGameRoomBlackList(GameRoom gameRoom) {
		return GameRoomBlackList.builder()
			.gameRoom(gameRoom)
			.build();
	}
}

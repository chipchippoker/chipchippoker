package com.chipchippoker.backend.common.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
public class SpectateRoom extends BaseEntity {
	@OneToMany(mappedBy = "spectateRoom", cascade = CascadeType.ALL)
	private List<Member> members = new ArrayList<>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_room_id")
	private GameRoom gameRoom;

	public static SpectateRoom createSpectateRoom(GameRoom gameRoom) {
		return SpectateRoom.builder()
			.gameRoom(gameRoom)
			.build();
	}
}

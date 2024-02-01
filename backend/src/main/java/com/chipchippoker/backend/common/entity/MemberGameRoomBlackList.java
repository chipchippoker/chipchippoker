package com.chipchippoker.backend.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGameRoomBlackList extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id")
	private GameRoom gameRoom;

	public static MemberGameRoomBlackList createMemberGameRoomBlackList(Member member,
		GameRoom gameRoom) {
		return MemberGameRoomBlackList.builder()
			.member(member)
			.gameRoom(gameRoom)
			.build();
	}
}

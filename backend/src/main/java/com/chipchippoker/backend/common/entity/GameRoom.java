package com.chipchippoker.backend.common.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class GameRoom extends BaseEntity {
	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "password")
	private Integer password;

	@Column(name = "total_participant_cnt", nullable = false)
	private Integer totalParticipantCnt;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "room_manager_nickname")
	private String roomManagerNickname;

	@Column(name = "is_private", nullable = false)
	private Boolean isPrivate;

	@Column(name = "state", nullable = false)
	private String state;

	@OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL)
	private List<Member> members = new ArrayList<>();

	@OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL)
	private List<MemberGameRoomBlackList> memberGameRoomBlackLists = new ArrayList<>();

	@OneToOne(mappedBy = "gameRoom", cascade = CascadeType.ALL)
	private SpectateRoom spectateRoom;

	public static GameRoom createGameRoom(String title, Integer password, Integer totalParticipantCnt,
		Boolean isPrivate, String type, String nickname) {
		return GameRoom.builder()
			.title(title)
			.password(password)
			.totalParticipantCnt(totalParticipantCnt)
			.type(type)
			.roomManagerNickname(nickname)
			.isPrivate(isPrivate)
			.state("대기")
			.build();
	}

	public void updateGameRoomState(String state) {
		this.state = state;
	}

	public void updateGameRoomManagerNickname(String nickname) {
		this.roomManagerNickname = nickname;
	}
}

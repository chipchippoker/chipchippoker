package com.chipchippoker.backend.common.entity;

import java.util.ArrayList;
import java.util.List;


import com.chipchippoker.backend.api.member.model.dto.SignupRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Member extends BaseEntity {
	@Column(name = "member_id")
	private String memberId;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "kakao_link_state", nullable = false)
	private Boolean kakaoLinkState = Boolean.FALSE;

	@Column(name = "kakao_friend_list_agreement", nullable = false)
	private Boolean kakaoFriendListAgreement = Boolean.FALSE;

	@Column(name = "kakao_social_id")
	private Long kakaoSocialId;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "icon", nullable = false)
	private String icon;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberGameRoomBlackList> memberGameRoomBlackLists = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_room_id")
	private GameRoom gameRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spectate_room_id")
	private SpectateRoom spectateRoom;

	@OneToMany(mappedBy = "memberA", cascade = CascadeType.ALL)
	private List<Friend> friends = new ArrayList<>();

	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private Point point;

	@OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL)
	private List<FriendRequest> toFriendRequest = new ArrayList<>();

	@OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL)
	private List<FriendRequest> fromFriendRequest = new ArrayList<>();

	public void enterGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}

	//비밀번호 암호화 메소드
	// public void passwordEncode(PasswordEncoder passwordEncoder) {
	// 	this.password = passwordEncoder.encode(this.password);
	// }

	public static Member newMember(SignupRequest signupRequest) {
		return Member.builder()
			.kakaoFriendListAgreement(Boolean.FALSE)
			.kakaoLinkState(Boolean.FALSE)
			.memberId(signupRequest.getMemberId())
			.password(signupRequest.getPassword())
			.nickname(signupRequest.getNickname())
			.icon(signupRequest.getIcon())
			.build();
	}


	public void updateRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}
}

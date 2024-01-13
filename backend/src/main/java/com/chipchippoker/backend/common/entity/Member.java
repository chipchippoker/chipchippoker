package com.chipchippoker.backend.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
}

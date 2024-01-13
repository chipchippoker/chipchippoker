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
public class GameRoom extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "password")
    private Integer password;

    @Column(name = "total_participant_cnt", nullable = false)
    private Integer totalParticipantCnt;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "room_manager_nickname", nullable = false)
    private String roomManagerNickname;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @Column(name = "state", nullable = false)
    private String state;

    @OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "gameRoom", cascade = CascadeType.ALL)
    private List<GameRoomBlackList> gameRoomBlackLists = new ArrayList<>();

    @OneToOne(mappedBy = "gameRoom", cascade = CascadeType.ALL)
    private SpectateRoom spectateRoom;
}

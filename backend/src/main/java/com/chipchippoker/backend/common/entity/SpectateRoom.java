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
public class SpectateRoom extends BaseEntity {
    @OneToMany(mappedBy = "spectateRoom", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_room_id")
    private GameRoom gameRoom;
}

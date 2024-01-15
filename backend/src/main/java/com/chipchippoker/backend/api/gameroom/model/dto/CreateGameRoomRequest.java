package com.chipchippoker.backend.api.gameroom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRoomRequest {
    private String title;
    private Boolean isPrivate;
    private Integer password;
    private Integer totalParticipantCnt;
}

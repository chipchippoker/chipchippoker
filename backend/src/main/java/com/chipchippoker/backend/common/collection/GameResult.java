package com.chipchippoker.backend.common.collection;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "game_result")
@ToString
@Getter
public class GameResult {
    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Long memberAId;
    private Long memberBId;
    private Long memberCId;
    private Long memberDId;
    private Integer memberACoin;
    private Integer memberBCoin;
    private Integer memberCCoin;
    private Integer memberDCoin;
    private Integer gameRoomId;
    private Integer totalParticipantCnt;
}

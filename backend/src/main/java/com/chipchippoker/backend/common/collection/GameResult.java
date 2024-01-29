package com.chipchippoker.backend.common.collection;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;

@Document(collection = "game_result")
@Getter
@Builder
public class GameResult {
	@Id
	private String id;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;
	private List<String> memberList;
	private List<Integer> resultCoinList;
	private Long gameRoomId;
	private String gameMode;

	public static GameResult newGameResult(List<String> memberList, List<Integer> resultCoinList, Long gameRoomId,
		String gameMode) {
		return GameResult.builder()
			.memberList(memberList)
			.resultCoinList(resultCoinList)
			.gameRoomId(gameRoomId)
			.gameMode(gameMode)
			.build();
	}
}

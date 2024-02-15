package com.chipchippoker.backend.api.member.dto.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class RecentPlayListResponse {

	Map<String, String> opponents;
	String gameMode;
	Integer memberNum;
	Integer pointChange;

	public static RecentPlayListResponse createRecentPlayListResponse(Map<String, String> opponents, String gameMode,
		Integer memberNum, Integer pointChange) {
		return RecentPlayListResponse.builder()
			.opponents(opponents)
			.gameMode(gameMode)
			.memberNum(memberNum)
			.pointChange(pointChange)
			.build();
	}
}

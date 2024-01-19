package com.chipchippoker.backend.api.member.dto.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class RecentPlayListResponse {

	Map<String,String> players;
	String gameMode;
	Integer memberNum;
	Integer pointChange;

	public static RecentPlayListResponse createRecentPlayListResponse(Map<String, String> players,String gameMode,Integer memberNum,Integer pointChange){
		return RecentPlayListResponse.builder()
			.players(players)
			.gameMode(gameMode)
			.memberNum(memberNum)
			.pointChange(pointChange)
			.build();
	}
}

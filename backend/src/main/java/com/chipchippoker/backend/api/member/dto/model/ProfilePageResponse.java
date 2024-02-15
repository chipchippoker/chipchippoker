package com.chipchippoker.backend.api.member.dto.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilePageResponse {
	String icon;
	Integer rank;
	Integer rankFriend;
	Integer friendlyTotal;
	Integer friendlyWin;
	Integer friendlyDraw;
	Integer friendlyLose;
	Double friendlyWinningRate;
	Integer competitiveTotal;
	Integer competitiveWin;
	Integer competitiveDraw;
	Integer competitiveLose;
	Double competitiveWinningRate;
	Integer maxWin;
	Integer point;
	String nickname;
	String tier;
	Boolean isMine;
	Boolean isFriend;
	Boolean isSent;
	Boolean isKakaoConnect;
	List<RecentPlayListResponse> recentPlayList;

	public static ProfilePageResponse createProfilePageResponse(String icon, Integer rank, Integer rankFriend,
		Integer friendlyWin, Integer friendlyDraw, Integer friendlyLose, Integer competitiveWin,
		Integer competitiveDraw, Integer competitiveLose,
		Integer maxWin, Integer point, String nickname, String tier, Boolean isMine, Boolean isFriend, Boolean isSent,
		Boolean isKakaoConnect,
		List<RecentPlayListResponse> recentPlayList) {
		return ProfilePageResponse.builder()
			.icon(icon)
			.rank(rank)
			.rankFriend(rankFriend)
			.friendlyTotal(friendlyWin + friendlyDraw + friendlyLose)
			.friendlyWin(friendlyWin)
			.friendlyDraw(friendlyDraw)
			.friendlyLose(friendlyLose)
			.friendlyWinningRate(
				Math.round((double)friendlyWin / (friendlyWin + friendlyLose) * 1000) / 10.0)
			.competitiveTotal(competitiveWin + competitiveDraw + competitiveLose)
			.competitiveWin(competitiveWin)
			.competitiveDraw(competitiveDraw)
			.competitiveLose(competitiveLose)
			.competitiveWinningRate(
				Math.round((double)competitiveWin / (competitiveWin + competitiveLose) * 1000) / 10.0)
			.maxWin(maxWin)
			.point(point)
			.nickname(nickname)
			.tier(tier)
			.isMine(isMine)
			.isFriend(isFriend)
			.isSent(isSent)
			.isKakaoConnect(isKakaoConnect)
			.recentPlayList(recentPlayList)
			.build();
	}
}

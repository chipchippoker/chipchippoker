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
	Integer total;
	Integer win;
	Integer draw;
	Integer lose;
	Double winningRate;
	Integer maxWin;
	Integer point;
	String nickname;
	String tier;
	Boolean isMine;
	Boolean isFriend;
	Boolean isSent;
	List<RecentPlayListResponse> recentPlayList;

	public static ProfilePageResponse createProfilePageResponse(String icon, Integer rank, Integer rankFriend,
		Integer win, Integer draw, Integer lose,
		Integer maxWin, Integer point, String nickname, String tier, Boolean isMine, Boolean isFriend, Boolean isSent,
		List<RecentPlayListResponse> recentPlayList) {
		return ProfilePageResponse.builder()
			.icon(icon)
			.rank(rank)
			.rankFriend(rankFriend)
			.total(win + draw + lose)
			.win(win)
			.draw(draw)
			.lose(lose)
			.winningRate(Math.round((double)win / lose * 10) / 10.0)
			.maxWin(maxWin)
			.point(point)
			.nickname(nickname)
			.tier(tier)
			.isMine(isMine)
			.isFriend(isFriend)
			.isSent(isSent)
			.recentPlayList(recentPlayList)
			.build();
	}
}

package com.chipchippoker.backend.api.friend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFriendListResponse {
	private String icon;
	private String tier;
	private String nickname;
	private Boolean isOnline;

	public static SearchFriendListResponse searchFriendListResponse(String icon, String tier, String nickname,
		Boolean isOnline) {
		return SearchFriendListResponse.builder()
			.icon(icon)
			.tier(tier)
			.nickname(nickname)
			.isOnline(isOnline)
			.build();
	}
}

package com.chipchippoker.backend.api.friend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFriendResponse {
	private String nickname;
	private String icon;
	private Boolean isOnline;
	private Boolean isFriend;
	private Boolean isSent;

	public static SearchFriendResponse searchFriendResponse(String nickname, String icon, Boolean isOnline,
		Boolean isFriend, Boolean isSent) {
		return SearchFriendResponse.builder()
			.nickname(nickname)
			.icon(icon)
			.isOnline(isOnline)
			.isFriend(isFriend)
			.isSent(isSent)
			.build();
	}
}

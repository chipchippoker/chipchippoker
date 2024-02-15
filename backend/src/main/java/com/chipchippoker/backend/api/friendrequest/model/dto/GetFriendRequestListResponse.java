package com.chipchippoker.backend.api.friendrequest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFriendRequestListResponse {
	private String nickname;

	public static GetFriendRequestListResponse getFriendRequestListResponse(String nickname) {
		return GetFriendRequestListResponse.builder()
			.nickname(nickname)
			.build();
	}
}

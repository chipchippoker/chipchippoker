package com.chipchippoker.backend.api.friendrequest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptFriendRequestRequest {
	private String nickname;
}

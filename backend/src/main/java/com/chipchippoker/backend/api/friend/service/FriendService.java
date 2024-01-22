package com.chipchippoker.backend.api.friend.service;

import java.util.List;

import com.chipchippoker.backend.api.friend.model.dto.SearchFriendListResponse;
import com.chipchippoker.backend.api.friend.model.dto.SearchFriendResponse;

public interface FriendService {
	SearchFriendResponse searchFriend(Long id, String nickname);

	List<SearchFriendListResponse> searchFriendList(Long id, String nickname);
}

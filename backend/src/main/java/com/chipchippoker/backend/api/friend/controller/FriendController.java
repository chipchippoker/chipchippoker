package com.chipchippoker.backend.api.friend.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.friend.model.dto.SearchFriendListResponse;
import com.chipchippoker.backend.api.friend.model.dto.SearchFriendResponse;
import com.chipchippoker.backend.api.friend.service.FriendService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/friends")
public class FriendController {
	private final HttpServletRequest request;
	private final FriendService friendService;

	// 친구 찾기 검색
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<?>> searchFriend(
		@RequestParam(value = "nickname") String nickname) {
		Long id = (Long)request.getAttribute("id");
		SearchFriendResponse searchFriendResponse = friendService.searchFriend(id, nickname);
		if (searchFriendResponse == null)
			return ResponseEntity.ok(ApiResponse.success(Collections.EMPTY_MAP));
		else
			return ResponseEntity.ok(ApiResponse.success(searchFriendResponse));
	}

	// 친구 찾기 목록 검색
	@GetMapping("/list/search")
	public ResponseEntity<ApiResponse<?>> searchFriendList(
		@RequestParam(value = "nickname") String nickname) {
		Long id = (Long)request.getAttribute("id");
		List<SearchFriendListResponse> searchFriendListResponses = friendService.searchFriendList(id, nickname);
		if (searchFriendListResponses == null)
			return ResponseEntity.ok(ApiResponse.success(Collections.EMPTY_LIST));
		else
			return ResponseEntity.ok(ApiResponse.success(searchFriendListResponses));
	}
}

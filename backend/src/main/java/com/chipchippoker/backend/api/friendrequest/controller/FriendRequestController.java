package com.chipchippoker.backend.api.friendrequest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.friendrequest.model.dto.AcceptFriendRequestRequest;
import com.chipchippoker.backend.api.friendrequest.model.dto.GetFriendRequestListResponse;
import com.chipchippoker.backend.api.friendrequest.model.dto.RejectFriendRequest;
import com.chipchippoker.backend.api.friendrequest.model.dto.RequestFriendRequest;
import com.chipchippoker.backend.api.friendrequest.service.FriendRequestService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/friends/request")
public class FriendRequestController {
	private final HttpServletRequest request;
	private final FriendRequestService friendRequestService;

	// 친구 신청
	@PostMapping("")
	public ResponseEntity<ApiResponse<Void>> requestFriend(@RequestBody RequestFriendRequest requestFriendRequest) {
		Long fromMemberId = (Long)request.getAttribute("id");
		friendRequestService.requestFriend(fromMemberId, requestFriendRequest.getNickname());
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 친구 신청 수락
	@PostMapping("/accept")
	public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(
		@RequestBody AcceptFriendRequestRequest acceptFriendRequestRequest) {
		Long toMemberId = (Long)request.getAttribute("id");
		friendRequestService.acceptFriendRequest(toMemberId, acceptFriendRequestRequest.getNickname());
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 친구 신청 거절
	@DeleteMapping("/reject")
	public ResponseEntity<ApiResponse<Void>> rejectFriendRequest(@RequestBody RejectFriendRequest rejectFriendRequest) {
		Long toMemberId = (Long)request.getAttribute("id");
		friendRequestService.rejectFriendRequest(toMemberId, rejectFriendRequest.getNickname());
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 친구 신청 요청 알림 목록
	@GetMapping("/list")
	public ResponseEntity<ApiResponse<List<GetFriendRequestListResponse>>> getFriendRequestList() {
		Long id = (Long)request.getAttribute("id");
		List<GetFriendRequestListResponse> responseList = friendRequestService.getFriendRequestList(
			id);
		return ResponseEntity.ok(ApiResponse.success(responseList));
	}
}

package com.chipchippoker.backend.api.gameroom.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.GetGameRoomListResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.LeaveGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.MemberOutGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.PlayGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.service.GameRoomService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GameRoomController {
	private final HttpServletRequest request;
	private final GameRoomService gameRoomService;

	// 방 생성
	@PostMapping("/api/rooms")
	public ResponseEntity<ApiResponse<CreateGameRoomResponse>> createGameRoom(
		@RequestBody @Valid CreateGameRoomRequest createGameRoomRequest) {
		Long id = (Long)request.getAttribute("id");
		CreateGameRoomResponse createGameRoomResponse = gameRoomService.createGameRoom(createGameRoomRequest, id);
		return ResponseEntity.ok(ApiResponse.success(createGameRoomResponse));
	}

	// 방 입장
	@PostMapping("/api/rooms/enter")
	public ResponseEntity<ApiResponse<EnterGameRoomResponse>> enterGameRoom(
		@RequestBody EnterGameRoomRequest enterGameRoomRequest) {
		Long id = (Long)request.getAttribute("id");
		EnterGameRoomResponse enterGameRoomResponse = gameRoomService.enterGameRoom(enterGameRoomRequest, id);
		return ResponseEntity.ok(ApiResponse.success(enterGameRoomResponse));
	}

	// 방 목록
	@GetMapping("/api/rooms")
	public ResponseEntity<ApiResponse<Page<GetGameRoomListResponse>>> getGameRoomList(
		@RequestParam(value = "type") String type,
		@RequestParam(value = "title") String title,
		@RequestParam(value = "isTwo") Boolean isTwo, @RequestParam(value = "isThree") Boolean isThree,
		@RequestParam(value = "isFour") Boolean isFour, @RequestParam(value = "isEmpty") Boolean isEmpty,
		Pageable pageable) {
		Page<GetGameRoomListResponse> getGameRoomListResponse = gameRoomService.getGameRoomList(type, title, isTwo,
			isThree, isFour,
			isEmpty, pageable);
		return ResponseEntity.ok(ApiResponse.success(getGameRoomListResponse));
	}

	// 방 나가기
	@PostMapping("/api/rooms/leave")
	public ResponseEntity<ApiResponse<Void>> leaveGameRoom(@RequestBody LeaveGameRoomRequest leaveGameRoomRequest) {
		Long id = (Long)request.getAttribute("id");
		gameRoomService.leaveGameRoom(leaveGameRoomRequest.getTitle(), id);
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 방 게임 시작하기
	@PostMapping("/api/rooms/play")
	public ResponseEntity<ApiResponse<Void>> playGameRoom(@RequestBody PlayGameRoomRequest playGameRoomRequest) {
		gameRoomService.playGameRoom(playGameRoomRequest.getTitle());
		return ResponseEntity.ok(ApiResponse.success());
	}

	// 방에서 사용자 강제 퇴장 시키기
	@PostMapping("/api/rooms/member/out")
	public ResponseEntity<ApiResponse<Void>> memberOutGameRoom(
		@RequestBody MemberOutGameRoomRequest memberOutGameRoomRequest) {
		Long id = (Long)request.getAttribute("id");
		gameRoomService.memberOutGameRoom(memberOutGameRoomRequest, id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}

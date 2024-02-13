package com.chipchippoker.backend.api.spectateroom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.spectateroom.model.dto.EnterSpectateRoomRequest;
import com.chipchippoker.backend.api.spectateroom.model.dto.EnterSpectateRoomResponse;
import com.chipchippoker.backend.api.spectateroom.service.SpectateRoomService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/spectate")
public class SpectateRoomController {
	private final HttpServletRequest request;
	private final SpectateRoomService spectateRoomService;

	@PostMapping("/enter")
	public ResponseEntity<ApiResponse<EnterSpectateRoomResponse>> enterSpectateRoom(
		@RequestBody EnterSpectateRoomRequest enterSpectateRoomRequest) {
		Long id = (Long)request.getAttribute("id");
		EnterSpectateRoomResponse enterSpectateRoomResponse = spectateRoomService.enterSpectateRoom(id,
			enterSpectateRoomRequest.getTitle(),
			enterSpectateRoomRequest.getPassword());
		return ResponseEntity.ok(ApiResponse.success(enterSpectateRoomResponse));
	}

	@PostMapping("leave")
	public ResponseEntity<ApiResponse<Void>> leaveSpectateRoom() {
		Long id = (Long)request.getAttribute("id");
		spectateRoomService.leaveSpectateRoom(id);
		return ResponseEntity.ok(ApiResponse.success());
	}
}

package com.chipchippoker.backend.api.matching.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingRequest;
import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;
import com.chipchippoker.backend.api.matching.service.MatchingService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/matching")
public class MatchingController {
	private final HttpServletRequest request;
	private final MatchingService matchingService;

	// 친선전 빠른 게임 시작
	@PostMapping("/friendly")
	public ResponseEntity<ApiResponse<?>> startFriendlyMatching(
		@RequestBody StartFriendlyMatchingRequest startFriendlyMatchingRequest) {
		Long id = (Long)request.getAttribute("id");
		StartFriendlyMatchingResponse startFriendlyMatchingResponse = matchingService.startFriendlyMatching(id,
			startFriendlyMatchingRequest.getTotalParticipantCnt());
		if (startFriendlyMatchingResponse == null)
			return ResponseEntity.ok(ApiResponse.success(Collections.EMPTY_MAP));
		else
			return ResponseEntity.ok(ApiResponse.success(startFriendlyMatchingResponse));
	}
}

package com.chipchippoker.backend.api.matching.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.matching.model.dto.QuitMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartCompetitionMatchingRequest;
import com.chipchippoker.backend.api.matching.model.dto.StartCompetitionMatchingResponse;
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

	@PostMapping("/competition")
	public ResponseEntity<ApiResponse<StartCompetitionMatchingResponse>> startCompetitionMatching(
		@RequestBody StartCompetitionMatchingRequest startCompetitionMatchingRequest) {
		Long id = (Long)request.getAttribute("id");
		StartCompetitionMatchingResponse startCompetitionMatchingResponse = matchingService.startCompetitionMatching(id,
			startCompetitionMatchingRequest.getTotalParticipantCnt());
		return ResponseEntity.ok(ApiResponse.success(startCompetitionMatchingResponse));
	}

	@PostMapping("/quit")
	public ResponseEntity<ApiResponse<QuitMatchingResponse>> quitMatching() {
		Long id = (Long)request.getAttribute("id");
		QuitMatchingResponse quitMatchingResponse = matchingService.quitMatching(id);
		return ResponseEntity.ok(ApiResponse.success(quitMatchingResponse));
	}
}

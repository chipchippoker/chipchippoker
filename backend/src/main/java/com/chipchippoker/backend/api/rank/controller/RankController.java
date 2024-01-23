package com.chipchippoker.backend.api.rank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.rank.model.dto.FriendRankResponse;
import com.chipchippoker.backend.api.rank.model.dto.TotalRankResponse;
import com.chipchippoker.backend.api.rank.service.RankService;
import com.chipchippoker.backend.common.dto.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Rank API Controller", description = "API 정보를 제공하는 랭크 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranks")
public class RankController {
	private final RankService rankService;
	private final HttpServletRequest request;

	@Operation(summary = "전체 랭킹 조회")
	@GetMapping("/totals")
	public ResponseEntity<ApiResponse<List<TotalRankResponse>>> getTotalRank() {
		return ResponseEntity.ok(ApiResponse.success(rankService.getTotalRank()));
	}

	@Operation(summary = "친구 랭킹 조회")
	@GetMapping("/friends")
	public ResponseEntity<ApiResponse<List<FriendRankResponse>>> getFriendRank() {
		Long id = (Long)request.getAttribute("id");
		return ResponseEntity.ok(ApiResponse.success(rankService.getFriendRank(id)));
	}
}

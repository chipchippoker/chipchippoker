package com.chipchippoker.backend.api.rank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TotalRankResponse {
	Integer rank;
	String tier;
	String icon;
	String nickname;
	Integer point;
}

package com.chipchippoker.backend.api.rank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRankResponse {
	Integer rank;
	String tier;
	String icon;
	String nickname;
	Integer point;
}

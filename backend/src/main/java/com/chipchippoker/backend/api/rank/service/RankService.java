package com.chipchippoker.backend.api.rank.service;

import java.util.List;

import com.chipchippoker.backend.api.rank.model.dto.FriendRankResponse;
import com.chipchippoker.backend.api.rank.model.dto.MyselfRankResponse;
import com.chipchippoker.backend.api.rank.model.dto.TotalRankResponse;

public interface RankService {
	List<TotalRankResponse> getTotalRank();

	List<FriendRankResponse> getFriendRank(Long id);

	MyselfRankResponse getMyselfRank(Long id);
}

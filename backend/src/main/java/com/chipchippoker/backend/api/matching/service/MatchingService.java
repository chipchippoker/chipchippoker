package com.chipchippoker.backend.api.matching.service;

import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;

public interface MatchingService {
	StartFriendlyMatchingResponse startFriendlyMatching(Long id, Integer totalParticipantCnt);
}

package com.chipchippoker.backend.api.matching.service;

import com.chipchippoker.backend.api.matching.model.dto.QuitMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartCompetitionMatchingResponse;
import com.chipchippoker.backend.api.matching.model.dto.StartFriendlyMatchingResponse;

public interface MatchingService {
	StartFriendlyMatchingResponse startFriendlyMatching(Long id, Integer totalParticipantCnt);

	StartCompetitionMatchingResponse startCompetitionMatching(Long id, Integer totalParticipantCnt);

	QuitMatchingResponse quitMatching(Long id);
}

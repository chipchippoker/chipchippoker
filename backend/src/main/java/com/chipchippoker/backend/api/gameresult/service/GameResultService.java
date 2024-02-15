package com.chipchippoker.backend.api.gameresult.service;

import java.util.List;

public interface GameResultService {
	void saveGameResult(List<String> memberList, List<Integer> resultCoinList, Long gameRoomId,
		String gameMode);
}

package com.chipchippoker.backend.api.gameresult.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;
import com.chipchippoker.backend.common.collection.GameResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GameResultServiceImpl implements GameResultService {
	private final GameResultRepository gameResultRepository;

	public void saveGameResult(List<String> memberList, List<Integer> resultCoinList, Long gameRoomId,
		String gameMode) {
		GameResult gameResult = GameResult.newGameResult(memberList, resultCoinList, gameRoomId, gameMode);
		gameResultRepository.save(gameResult);
	}
}

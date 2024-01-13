package com.chipchippoker.backend.api.gameresult.service;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameResultServiceImpl implements GameResultService{
    private final GameResultRepository gameResultRepository;
}

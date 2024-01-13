package com.chipchippoker.backend.api.gameresult.controller;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameResultController {
    private final GameResultRepository gameResultRepository;

    @RequestMapping("/gameresult")
    public Integer getCoin() {
        return gameResultRepository.getCoin();
    }
}

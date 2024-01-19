package com.chipchippoker.backend.api.gameresult.controller;

import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameResultController {
    private final GameResultRepository gameResultRepository;

    // @RequestMapping("/gameresult")
    // public Integer getCoin() {
    //     return gameResultRepository.getCoin();
    // }
}

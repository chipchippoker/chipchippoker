package com.chipchippoker.backend.common.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.chipchippoker.backend.websocket.game.model.GameManager;
import com.chipchippoker.backend.websocket.spectation.model.SpectationManager;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Slf4j
public class MapManager {
	private Map<String, GameManager> gameManagerMap;
	private Map<String, String> sessionMap;
	private Map<String, SpectationManager> spectationManagerMap;

	@PostConstruct
	public void init() {
		gameManagerMap = new ConcurrentHashMap<>();
		sessionMap = new ConcurrentHashMap<>();
		spectationManagerMap = new ConcurrentHashMap<>();
	}
}
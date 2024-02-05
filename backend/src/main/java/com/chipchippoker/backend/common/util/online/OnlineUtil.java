package com.chipchippoker.backend.common.util.online;

import org.springframework.stereotype.Component;

import com.chipchippoker.backend.common.manager.MapManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnlineUtil {
	private final MapManager mapManager;

	public boolean isOnline(String nickname) {
		return mapManager.getSessionMap().containsValue(nickname);
	}
}

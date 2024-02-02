package com.chipchippoker.backend.common.util.online;

import org.springframework.stereotype.Component;

import com.chipchippoker.backend.common.handler.InboundChannelInterceptor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OnlineUtil {
	public boolean isOnline(String nickname) {
		return InboundChannelInterceptor.sessionMap.containsValue(nickname);
	}
}

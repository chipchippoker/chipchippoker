package com.chipchippoker.backend.common.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.common.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
	private final JwtUtil jwtUtil;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String accessToken = accessor.getFirstNativeHeader("access-token");
		String refreshToken = accessor.getFirstNativeHeader("refresh-token");
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			jwtUtil.getId(accessToken, refreshToken);
		}
		return message;
	}
}

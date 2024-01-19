package com.chipchippoker.backend.common.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;
	private static final Map<String, String> sessionMap = new HashMap<>();

	/*
	connect 할 때 호출
	subscribe 할 때 호출
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("preSend");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			log.info("CONNECT");
			log.info(accessor.getFirstNativeHeader("access-token"));
			String nickname = jwtUtil.getNickName(accessor.getFirstNativeHeader("access-token"),
				accessor.getFirstNativeHeader("refresh-token"));
			sessionMap.put(sessionId, nickname);
		} else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && Objects.requireNonNull(
			accessor.getDestination()).contains("member")) {
			log.info("SUBSCRIBE MEMBER");
			String nickname = sessionMap.get(accessor.getSessionId());
			if (!nickname.equals(accessor.getDestination().split("/member/")[1])) {
				return null;
			}
		}
		// todo (환) 방을 구독하는 것을 제한하는 검증 로직 나중에 추가할 것
		// else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) &&
		// 	Objects.requireNonNull(accessor.getDestination()).contains("checkConnect")) {
		// 	log.info("SUBSCRIBE GAME ROOM");
		// 	String nickname = sessionMap.get(accessor.getSessionId());
		// 	Member member = memberRepository.findByNickname(nickname).orElseGet(null);
		// 	if (member == null) {
		// 		return null;
		// 	} else if (member.getGameRoom() == null) {
		// 		return null;
		// 	} else if (!member.getGameRoom().getTitle().equals(accessor.getDestination().split("/checkConnect/")[1])) {
		// 		return null;
		// 	}
		// }
		return message;
	}

	/*
	connect 할 때 호출
	subscribe 할 때 호출
	disconnect 할 때 호출
	클라이언트에게 메시지를 받을 때 호출
	 */
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		log.info("postSend");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		log.info(sessionId);
		if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
			sessionMap.remove(sessionId);
		}
		ChannelInterceptor.super.postSend(message, channel, sent);
	}

	/*
	connect 할 때 호출
	subscribe 할 때 호출
	disconnect 할 때 호출
	클라이언트에게 메시지 받을 때 호출
	 */
	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		log.info("afterSendCompletion");
		ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
	}

	@Override
	public boolean preReceive(MessageChannel channel) {
		log.info("preReceive");
		return ChannelInterceptor.super.preReceive(channel);
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		log.info("postReceive");
		return ChannelInterceptor.super.postReceive(message, channel);
	}

	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		log.info("afterReceiveCompletion");
		ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
	}
}

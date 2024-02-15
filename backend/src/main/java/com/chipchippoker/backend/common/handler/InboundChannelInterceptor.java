package com.chipchippoker.backend.common.handler;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.common.manager.MapManager;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboundChannelInterceptor implements ChannelInterceptor {
	private final JwtUtil jwtUtil;
	private final MapManager mapManager;

	/*
	connect 할 때 호출
	subscribe 할 때 호출
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		log.info("preSend");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		SimpMessageType messageType = accessor.getMessageType();

		// 웹소켓이 이미 연결된 상태이고 연결을 확인하는 메시지를 전송하는 경우
		if (SimpMessageType.HEARTBEAT.equals(messageType)) {
			log.info("HEART BEAT");
		}
		/*
		웹 소켓 연결을 시도하는 경우
		1. 액세스 토큰을 확인
		2. 액세스 토큰에서 닉네임을 추출한다.
		3. 각 닉네임 당 하나의 세션을 보유할 수 있다.
		 */
		else if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			log.info("CONNECT");
			log.info(accessor.getFirstNativeHeader("access-token"));
			String nickname = jwtUtil.getNickname(accessor.getFirstNativeHeader("access-token"));
			mapManager.getSessionMap().put(sessionId, nickname);
			log.info(nickname.concat("이 연결된 세션 ID는 ").concat(sessionId).concat(" 입니다."));
		}
		/*
		회원 개인의 메시지함을 구독하는 경우
		1. 커맨드가 구독이고 구독 목표가 member를 포함하는 지 확인한다.
		2. 액세스 토큰에서 닉네임을 추출한다.
		3. 구독하려는 메시지함과 메시지를 보낸 사람이 일치하지 않는 경우 NULL 을 반환하여 구독을 할 수 없게한다.
		 */
		else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && Objects.requireNonNull(
			accessor.getDestination()).contains("member")) {
			log.info("SUBSCRIBE MEMBER");
			String nickname = mapManager.getSessionMap().get(sessionId);
			log.info("nickname : ".concat(nickname));
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
	웹소켓 연결을 끊는 로직은 내부적으로 두 번 발생하기 때문에 멱등성을 위해 세션연결이 존재하는 지 추가했다
	 */
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		log.info("postSend");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();
		log.info(sessionId);
		if (StompCommand.DISCONNECT.equals(accessor.getCommand())
			&& !(mapManager.getSessionMap().get(sessionId) == null)) {
			log.info("DISCONNECT");
			String nickname = mapManager.getSessionMap().get(sessionId);
			mapManager.getSessionMap().remove(sessionId);
			log.info(nickname.concat("의 웹소켓의 연결을 종료합니다."));
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

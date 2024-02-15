package com.chipchippoker.backend.websocket.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.chipchippoker.backend.common.dto.ApiResponse;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.websocket.notification.dto.FriendRequestMessageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
	private final JwtUtil jwtUtil;
	private final SimpMessagingTemplate template;

	@MessageMapping("/friend/request")
	public void createFriendRequest(
		@Header(name = "access-token") String accessToken,
		FriendRequestMessageRequest friendRequestMessageRequest
	) {
		jwtUtil.isValidateAccessToken(accessToken);
		log.info("친구요청 보내기 시작");
		broadcastToMember(friendRequestMessageRequest.getNickname(), ResponseEntity.ok(
			ApiResponse.messageSuccess(MessageBase.S200_FRIEND_REQUEST)
		));
	}

	private void broadcastToMember(String nickname, Object object) {
		log.info("개인에게 메시지 전송");
		template.convertAndSend("/from/chipchippoker/member/".concat(nickname), object);
	}
}

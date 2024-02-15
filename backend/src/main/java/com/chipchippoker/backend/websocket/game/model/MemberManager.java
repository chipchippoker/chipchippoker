package com.chipchippoker.backend.websocket.game.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberManager {
	private MemberInfo memberInfo;
	private MemberGameInfo memberGameInfo;
	private LocalDateTime createdAt;

	public static MemberManager create(MemberInfo memberInfo, MemberGameInfo memberGameInfo) {
		return MemberManager.builder()
			.memberInfo(memberInfo)
			.memberGameInfo(memberGameInfo)
			.createdAt(LocalDateTime.now())
			.build();
	}
}

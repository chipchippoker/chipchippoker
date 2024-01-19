package com.chipchippoker.backend.websocket.game.model;

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
/*
회원 개인 정보
레디 상태
라운드 카드정보
보유 코인
현재 상태(CALL, RUN, DIE)
방장 여부
 */
public class MemberManager {
	private MemberInfo memberInfo;
	private MemberGameInfo memberGameInfo;

	public static MemberManager create(MemberInfo memberInfo, MemberGameInfo memberGameInfo) {
		return MemberManager.builder()
			.memberInfo(memberInfo)
			.memberGameInfo(memberGameInfo)
			.build();
	}
}

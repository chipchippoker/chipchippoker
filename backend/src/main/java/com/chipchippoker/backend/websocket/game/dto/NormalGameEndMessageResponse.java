package com.chipchippoker.backend.websocket.game.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chipchippoker.backend.websocket.game.model.MemberEndGameInfo;
import com.chipchippoker.backend.websocket.game.model.MemberManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NormalGameEndMessageResponse {
	List<MemberEndGameInfo> memberEndGameInfos = new ArrayList<>();

	public static NormalGameEndMessageResponse create(Collection<MemberManager> memberManagers) {
		List<MemberEndGameInfo> result = new ArrayList<>();

		for (MemberManager manager : memberManagers) {
			MemberEndGameInfo memberEndGameInfo = new MemberEndGameInfo(manager.getMemberInfo().getNickname(),
				manager.getMemberGameInfo().getHaveCoin() == 25 ? "무" :
					manager.getMemberGameInfo().getHaveCoin() > 25 ? "승" : "패",
				null
			);
			result.add(memberEndGameInfo);
		}

		return NormalGameEndMessageResponse.builder()
			.memberEndGameInfos(result)
			.build();
	}
}

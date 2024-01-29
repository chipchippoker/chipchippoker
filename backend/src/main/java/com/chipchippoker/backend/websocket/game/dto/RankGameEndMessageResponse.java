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
public class RankGameEndMessageResponse {
	List<MemberEndGameInfo> memberEndGameInfos = new ArrayList<>();

	public static RankGameEndMessageResponse create(Collection<MemberManager> memberManagers) {
		List<MemberEndGameInfo> result = new ArrayList<>();

		for (MemberManager manager : memberManagers) {
			MemberEndGameInfo memberEndGameInfo = new MemberEndGameInfo(manager.getMemberInfo().getNickname(),
				manager.getMemberGameInfo().getHaveCoin() == 25 ? "무" :
					manager.getMemberGameInfo().getHaveCoin() > 25 ? "승" : "패",
				((manager.getMemberGameInfo().getHaveCoin() / 25) * 10) +
					(manager.getMemberGameInfo().getHaveCoin() - 25)
			);
			result.add(memberEndGameInfo);
		}

		return RankGameEndMessageResponse.builder()
			.memberEndGameInfos(result)
			.build();
	}
}

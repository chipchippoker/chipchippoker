package com.chipchippoker.backend.api.membergameroomblacklist.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.MemberGameRoomBlackList;

public interface MemberGameRoomBlackListRepository
	extends JpaRepository<MemberGameRoomBlackList, Long>, MemberGameRoomBlackListRepositoryCustom {
	MemberGameRoomBlackList findByMemberAndGameRoom(Member member, GameRoom gameRoom);
}

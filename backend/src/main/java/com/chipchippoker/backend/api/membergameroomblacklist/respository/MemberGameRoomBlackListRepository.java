package com.chipchippoker.backend.api.membergameroomblacklist.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.MemberGameRoomBlackList;

public interface MemberGameRoomBlackListRepository
	extends JpaRepository<MemberGameRoomBlackList, Long>, MemberGameRoomBlackListRepositoryCustom {
	MemberGameRoomBlackList findByGameRoomBlackListIdAndMemberId(Long gameRoomBlackListId, Long memberId);
}

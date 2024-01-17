package com.chipchippoker.backend.api.gameroomblacklist.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.GameRoomBlackList;

public interface GameRoomBlackListRepository
	extends JpaRepository<GameRoomBlackList, Long>, GameRoomBlackListRepositoryCustom {
	Optional<GameRoomBlackList> findByGameRoomId(Long id);
}

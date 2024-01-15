package com.chipchippoker.backend.api.gameroom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.GameRoom;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long>, GameRoomRepositoryCustom {
	Optional<GameRoom> findByTitle(String title);
}

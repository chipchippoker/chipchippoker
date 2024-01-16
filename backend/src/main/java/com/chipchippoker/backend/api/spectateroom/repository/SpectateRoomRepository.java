package com.chipchippoker.backend.api.spectateroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.SpectateRoom;

public interface SpectateRoomRepository extends JpaRepository<SpectateRoom, Long>, SpectateRoomRepositoryCustom {
}

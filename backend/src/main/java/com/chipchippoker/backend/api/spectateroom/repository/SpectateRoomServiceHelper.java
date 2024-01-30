package com.chipchippoker.backend.api.spectateroom.repository;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.SpectateRoom;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SpectateRoomServiceHelper {
	public static void isExistSpectateRoom(SpectateRoom spectateRoom) {
		if (spectateRoom == null)
			throw new NotFoundException(ErrorBase.E404_NOT_EXISTS);
	}
}

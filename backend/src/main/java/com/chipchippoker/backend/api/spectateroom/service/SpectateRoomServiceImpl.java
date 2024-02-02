package com.chipchippoker.backend.api.spectateroom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.gameroom.service.GameRoomServiceHelper;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.spectateroom.model.dto.EnterSpectateRoomResponse;
import com.chipchippoker.backend.api.spectateroom.repository.SpectateRoomRepository;
import com.chipchippoker.backend.api.spectateroom.repository.SpectateRoomServiceHelper;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.SpectateRoom;
import com.chipchippoker.backend.common.exception.ForbiddenException;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SpectateRoomServiceImpl implements SpectateRoomService {
	private final MemberRepository memberRepository;
	private final GameRoomRepository gameRoomRepository;
	private final SpectateRoomRepository spectateRoomRepository;

	@Override
	public EnterSpectateRoomResponse enterSpectateRoom(Long id, String title, Integer password) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		// 관전방에 이미 들어가 있는 사용자인 경우
		if (member.getSpectateRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_SPECTATE_ROOM);
		}

		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(title);
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우

		SpectateRoom spectateRoom = spectateRoomRepository.findByGameRoom(gameRoom);
		SpectateRoomServiceHelper.isExistSpectateRoom(spectateRoom);

		// 비공개 게임방인 경우, 비밀번호 확인
		if (gameRoom.getIsPrivate())
			GameRoomServiceHelper.isCorrectGameRoomPassword(password, gameRoom.getPassword());

		// 관전 최대 인원은 6명이기 때문에, 인원 초과 여부 확인
		if (spectateRoom.getMembers().size() > 5)
			throw new ForbiddenException(ErrorBase.E403_ALREADY_FULL_SPECTATE_ROOM);

		// 관전방 입장
		member.enterSpectateRoom(spectateRoom);

		return EnterSpectateRoomResponse.enterSpectateRoomResponse(gameRoom.getId(), gameRoom.getTitle(),
			gameRoom.getState());
	}

	@Override
	public void leaveSpectateRoom(Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		member.leaveSpectateRoom();
	}
}

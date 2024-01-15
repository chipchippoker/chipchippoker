package com.chipchippoker.backend.api.gameroom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomServiceImpl implements GameRoomService {
	private final GameRoomRepository gameRoomRepository;
	private final MemberRepository memberRepository;

	public CreateGameRoomResponse createGameRoom(CreateGameRoomRequest createGameRoomRequest) {
		String nickname = "sun"; // TODO: 추후 nickname을 access-token에서 받아오기 필요
		Member member = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		GameRoomServiceHelper.isDuplicatedGameRoom(gameRoomRepository,
			createGameRoomRequest.getTitle()); // 이미 중복되는 방제목이 있는 경우

		List<Member> members = new ArrayList<>();
		members.add(member);
		GameRoom gameRoom = GameRoom.createGameRoom(createGameRoomRequest, members, nickname);
		gameRoomRepository.save(gameRoom);

		member.enterGameRoom(gameRoom);

		return CreateGameRoomResponse.createGameRoomResponse(gameRoom);
	}

}

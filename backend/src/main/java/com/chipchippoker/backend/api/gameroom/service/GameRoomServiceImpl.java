package com.chipchippoker.backend.api.gameroom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.CreateGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.model.dto.EnterGameRoomResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.GetGameRoomListResponse;
import com.chipchippoker.backend.api.gameroom.model.dto.MemberOutGameRoomRequest;
import com.chipchippoker.backend.api.gameroom.repository.GameRoomRepository;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.api.membergameroomblacklist.respository.MemberGameRoomBlackListRepository;
import com.chipchippoker.backend.api.spectateroom.repository.SpectateRoomRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.GameRoom;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.MemberGameRoomBlackList;
import com.chipchippoker.backend.common.entity.SpectateRoom;
import com.chipchippoker.backend.common.exception.ForbiddenException;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomServiceImpl implements GameRoomService {
	private final GameRoomRepository gameRoomRepository;
	private final MemberRepository memberRepository;
	private final SpectateRoomRepository spectateRoomRepository;
	private final MemberGameRoomBlackListRepository memberGameRoomBlackListRepository;

	public CreateGameRoomResponse createGameRoom(CreateGameRoomRequest createGameRoomRequest, Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		String nickname = member.getNickname();

		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}

		Integer gameRoomListSize = gameRoomRepository.findByStateCnt();
		if (gameRoomListSize >= 100)
			throw new ForbiddenException(ErrorBase.E403_OVER_MAX_GAME_ROOM_CNT);

		GameRoomServiceHelper.isDuplicatedGameRoom(gameRoomRepository,
			createGameRoomRequest.getTitle());

		GameRoom gameRoom = GameRoom.createGameRoom(createGameRoomRequest.getTitle(),
			createGameRoomRequest.getPassword(), createGameRoomRequest.getTotalParticipantCnt(),
			createGameRoomRequest.getIsPrivate(), "친선", nickname);
		gameRoomRepository.save(gameRoom);

		member.enterGameRoom(gameRoom);

		SpectateRoom spectateRoom = SpectateRoom.createSpectateRoom(gameRoom);
		spectateRoomRepository.save(spectateRoom);

		return CreateGameRoomResponse.createGameRoomResponse(gameRoom);
	}

	public EnterGameRoomResponse enterGameRoom(EnterGameRoomRequest enterGameRoomRequest, Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(enterGameRoomRequest.getTitle());
		GameRoomServiceHelper.isExistGameRoom(gameRoom);

		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}
		GameRoomServiceHelper.isBlackListMember(memberGameRoomBlackListRepository, member, gameRoom);
		if (gameRoom.getIsPrivate())
			GameRoomServiceHelper.isCorrectGameRoomPassword(enterGameRoomRequest.getPassword(), gameRoom.getPassword());
		GameRoomServiceHelper.isStartedGameRoom(gameRoom.getState());
		GameRoomServiceHelper.isFullGameRoom(gameRoom);

		member.enterGameRoom(gameRoom);
		return EnterGameRoomResponse.enterGameRoomResponse(gameRoom);
	}

	public Page<GetGameRoomListResponse> getGameRoomList(String type, String title, Boolean isTwo, Boolean isThree,
		Boolean isFour,
		Boolean isEmpty, Pageable pageable) {
		Page<GameRoom> gameRoomList = gameRoomRepository.findBySearchOption(type, title, isTwo, isThree, isFour,
			isEmpty, pageable);
		return gameRoomList.map(
			gameRoom -> {
				Integer currentParticipantCnt = gameRoom.getMembers().size();
				Integer currentSpectatorCnt = type.equals("경쟁") ? 0 : gameRoom.getSpectateRoom().getMembers().size();

				return GetGameRoomListResponse.gameRoomListResponse(gameRoom.getId(), gameRoom.getIsPrivate(),
					gameRoom.getState(), gameRoom.getTitle(), gameRoom.getTotalParticipantCnt(), currentParticipantCnt,
					currentSpectatorCnt);
			});
	}

	public void leaveGameRoom(String title, Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(title);
		GameRoomServiceHelper.isExistGameRoom(gameRoom);

		if (member.getNickname().equals(gameRoom.getRoomManagerNickname())) {
			if (gameRoom.getMembers().size() == 1) {
				gameRoom.updateGameRoomState("종료");
			} else {
				for (Member leftMember : gameRoom.getMembers()) {
					if (!leftMember.equals(member)) {
						gameRoom.updateGameRoomManagerNickname(leftMember.getNickname());
						break;
					}
				}
			}
		}

		member.leaveGameRoom();
		memberRepository.save(member);
	}

	public void playGameRoom(String title) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(title);
		GameRoomServiceHelper.isExistGameRoom(gameRoom);
		gameRoom.updateGameRoomState("진행");
	}

	public void memberOutGameRoom(MemberOutGameRoomRequest memberOutGameRoomRequest, Long id) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(memberOutGameRoomRequest.getTitle());
		GameRoomServiceHelper.isExistGameRoom(gameRoom);

		Member requestMember = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoomServiceHelper.isGameRoomManager(requestMember.getNickname(), gameRoom.getRoomManagerNickname());

		// 방에서 강제 퇴장 시키기
		Member leavingMember = memberRepository.findByNickname(memberOutGameRoomRequest.getNickname())
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		leavingMember.leaveGameRoom();
		memberRepository.save(leavingMember);

		MemberGameRoomBlackList memberGameRoomBlackList = MemberGameRoomBlackList.createMemberGameRoomBlackList(
			leavingMember, gameRoom);
		memberGameRoomBlackListRepository.save(memberGameRoomBlackList);
	}

	@Override
	public List<String> findMemberNicknames(String gameRoomTitle) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(gameRoomTitle);
		GameRoomServiceHelper.isExistGameRoom(gameRoom);
		List<Member> members = gameRoom.getMembers();
		return members.stream().map(Member::getNickname).collect(Collectors.toList());

	}
}

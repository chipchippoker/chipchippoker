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
import com.chipchippoker.backend.common.exception.DuplicateException;
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

		// 게임방에 이미 들어가 있는 사용자인 경우
		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}

		// 방 개수가 100개를 초과하는 경우, 생성 불가
		if (gameRoomRepository.findAll().size() >= 100)
			throw new DuplicateException(ErrorBase.E403_OVER_MAX_GAME_ROOM_CNT);

		GameRoomServiceHelper.isDuplicatedGameRoom(gameRoomRepository,
			createGameRoomRequest.getTitle()); // 이미 중복되는 방제목이 있는 경우

		GameRoom gameRoom = GameRoom.createGameRoom(createGameRoomRequest.getTitle(),
			createGameRoomRequest.getPassword(), createGameRoomRequest.getTotalParticipantCnt(),
			createGameRoomRequest.getIsPrivate(), "친선", nickname);
		gameRoomRepository.save(gameRoom);

		// 연관관계 매핑
		member.enterGameRoom(gameRoom);

		// 관전방 생성
		SpectateRoom spectateRoom = SpectateRoom.createSpectateRoom(gameRoom);
		spectateRoomRepository.save(spectateRoom);

		return CreateGameRoomResponse.createGameRoomResponse(gameRoom);
	}

	public EnterGameRoomResponse enterGameRoom(EnterGameRoomRequest enterGameRoomRequest, Long id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(enterGameRoomRequest.getTitle());
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우

		// 게임방에 이미 들어가 있는 사용자인 경우
		if (member.getGameRoom() != null) {
			throw new ForbiddenException(ErrorBase.E403_FORBIDDEN_ALREADY_IN_GAME_ROOM);
		}
		// 블랙리스트 사용자인 경우
		GameRoomServiceHelper.isBlackListMember(memberGameRoomBlackListRepository, member, gameRoom);
		// 게임 방 입장 비밀번호가 다른 경우
		if (gameRoom.getIsPrivate())
			GameRoomServiceHelper.isCorrectGameRoomPassword(enterGameRoomRequest.getPassword(), gameRoom.getPassword());
		// 이미 게임이 시작된 경우
		GameRoomServiceHelper.isStartedGameRoom(gameRoom.getState());
		// 게임 방 충원 인원이 모두 채워진 경우
		GameRoomServiceHelper.isFullGameRoom(gameRoom);

		// 입장 가능한 경우
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
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우

		if (member.getNickname().equals(gameRoom.getRoomManagerNickname())) { // 나가려는 사용자가 방장인 경우
			if (gameRoom.getMembers().size() == 1) { // 나가려는 방장이 남은 인원 중 마지막 인원인 경우
				gameRoom.updateGameRoomState("종료");
			} else { // 나가려는 방장이 남은 인원 중 마지막 인원이 아닌 경우
				// 방장을 제외한 방의 남은 인원 중 한명을 방장으로 변경
				for (Member leftMember : gameRoom.getMembers()) {
					if (!leftMember.equals(member)) {
						gameRoom.updateGameRoomManagerNickname(leftMember.getNickname());
						break;
					}
				}
			}
		}

		// 방 나가기
		member.leaveGameRoom();
		memberRepository.save(member);
	}

	public void playGameRoom(String title) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(title);
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우
		gameRoom.updateGameRoomState("진행");
	}

	public void memberOutGameRoom(MemberOutGameRoomRequest memberOutGameRoomRequest, Long id) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(memberOutGameRoomRequest.getTitle());
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우

		// 요청 사용자가 방장인지 확인
		Member requestMember = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		GameRoomServiceHelper.isGameRoomManager(requestMember.getNickname(), gameRoom.getRoomManagerNickname());

		// 방에서 강제 퇴장 시키기
		Member leavingMember = memberRepository.findByNickname(memberOutGameRoomRequest.getNickname())
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		leavingMember.leaveGameRoom();
		memberRepository.save(leavingMember);

		// blackList에 강제 퇴장 사용자 추가
		MemberGameRoomBlackList memberGameRoomBlackList = MemberGameRoomBlackList.createMemberGameRoomBlackList(
			leavingMember, gameRoom);
		memberGameRoomBlackListRepository.save(memberGameRoomBlackList);
	}

	@Override
	public List<String> findMemberNicknames(String gameRoomTitle) {
		GameRoom gameRoom = gameRoomRepository.findByTitleAndState(gameRoomTitle);
		GameRoomServiceHelper.isExistGameRoom(gameRoom); // 게임방이 존재하지 않는 경우
		List<Member> members = gameRoom.getMembers();
		return members.stream().map(Member::getNickname).collect(Collectors.toList());

	}
}

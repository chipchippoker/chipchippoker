package com.chipchippoker.backend.websocket.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.dto.MessageBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.websocket.game.dto.BettingMessageRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/*
  게임에 필요한 로직을 관리한다.
  필요한 요소는 아래와 같다.
  1. 게임 세팅
  1-1. 게임에 참여한 유저에 대한 정보(유저의 수, 각 유저의 정보)
  1-2. 게임상태(시작전, 시작후, 시작) O
  1-3. 각 유저의 레디 상태
  1-4. 몇 명이 참여 가능한 방인지에 대한 정보
  2. 현재 라운드의 상황
  2-1. 현재 몇 라운드인지
  2-2. 각 라운드에서 각 유저가 가지고 있는 패의 정보
  2-3. 각 라운드에서 각 유저가 가지고 있는 칩의 개수
  2-4. 각 라운드에서 각 유저가 배팅한 칩의 개수
  2-5. 각 라운드에서 가운데 배팅된 총 칩의 개수
  2-6. 각 라운드에서 누구의 배팅턴인지
  2-7. 각 라운드에서 누군가의 배팅시간이 얼마나 남았는 지
 */

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GameManager {
	private String roomTitle;
	private String gameState;
	private Integer countOfPeople;
	private Map<String, MemberManager> memberManagerMap;
	private Integer currentRound;
	private final Integer gameEndRound = 10;
	private final Integer bettingLimitTime = 15;
	private Stack<CardInfo> cardStack;
	private List<String> order;
	private Integer turnNumber;
	private String roomManager;

	public GameManager(String roomTitle, Integer countOfPeople, String nickname) {
		log.info("게임방을 생성합니다.");
		this.roomTitle = roomTitle;
		this.gameState = "대기";
		this.countOfPeople = countOfPeople;
		this.memberManagerMap = new HashMap<>();
		this.currentRound = 1;
		this.cardStack = makeCardStack(countOfPeople);
		this.order = new ArrayList<>();
		this.turnNumber = 0;
		this.roomManager = nickname;
		log.info("게임방을 생성했습니다.");
	}

	public boolean nextTurn() {
		for (int i = 0; i < 5; i++) {
			// 다음 사람이 배팅중이라면 턴을 넘겨준다.
			if (memberManagerMap.get(order.get((turnNumber + 1) % memberManagerMap.size()))
				.getMemberGameInfo()
				.getIsState()
				.equals("BET")) {
				setTurnNumber((getTurnNumber() + 1) % memberManagerMap.size());
				return true;
				// 다음 사람이 베팅중이 아니라면 그 다음 사람에게 턴을 넘겨줘야한다.
			} else {
				turnNumber = (turnNumber + 1) % memberManagerMap.size();
			}
		}
		return false;
	}

	private Stack<CardInfo> makeCardStack(int countOfPeople) {
		List<CardInfo> numbers = new ArrayList<>();
		for (int set = 1; set <= countOfPeople; set++) {
			for (int i = 1; i <= 10; i++) {
				numbers.add(new CardInfo(i, set));
			}
		}

		// Stack 생성
		// 각 세트에 대해 랜덤으로 숫자를 섞어서 Stack에 삽입
		Stack<CardInfo> stack = new Stack<>();
		// 리스트를 섞기
		Collections.shuffle(numbers, new Random());
		for (CardInfo cardInfo : numbers) {
			stack.push(cardInfo);
		}
		return stack;
	}

	public void gameStart() {
		// 게임이 이미 진행 중
		if (this.gameState.equals("진행"))
			throw new InvalidException(MessageBase.E400_CAN_NOT_START_ALREADY_START);

		// 모두 준비된 상태이고 최소인원인 2명이상을 충족한다.
		if (memberManagerMap.size() < 2) {
			throw new InvalidException(MessageBase.E400_CAN_NOT_START_ALONE);
		} else if (!isAllReady()) {
			throw new InvalidException(MessageBase.E400_CAN_NOT_START_NOT_READY);
		} else {
			this.gameState = "진행";
			this.order.addAll(memberManagerMap.keySet().stream().toList());
			// 카드 배분
			// 기본칩 배팅
			// 라운드 설정
			newRoundSetting(0);
		}
	}

	public void betting(BettingMessageRequest bettingMessageRequest, MemberManager memberManager) {
		// 유저가 이미 죽은 상태라면
		if (memberManager.getMemberGameInfo().getIsState().equals("DIE")) {
			throw new InvalidException(ErrorBase.E400_INVALID_ALREADY_DIE);
		}

		/*
		1. 본인이 가진 코인의 양에서 배팅을 했는가? 또는 음의 코인을 베팅하려 하는 가?
		2. 배팅해야하는 최소 칩 이상으로 배팅을 했는가?
		3. 배팅해야하는 최대 칩 이하로 배팅을 했는가?
		 */
		if (bettingMessageRequest.getAction().equals("BET")) {
			if (bettingMessageRequest.getBettingCoin() > memberManager.getMemberGameInfo().getHaveCoin()
				|| bettingMessageRequest.getBettingCoin() <= 0
			) {
				throw new InvalidException(ErrorBase.E400_INVALID_BET_COIN);
			}
			// todo (환) 배팅 가능한 코인을 배팅한 것인지 확인하는 로직

			// 배팅중
			memberManager.getMemberGameInfo().setIsState("BET");

			// 가진 코인 변경
			memberManager.getMemberGameInfo()
				.setHaveCoin(memberManager.getMemberGameInfo().getHaveCoin() - bettingMessageRequest.getBettingCoin());
			// 배팅한 코인 변경
			memberManager.getMemberGameInfo().setBettingCoin(memberManager.getMemberGameInfo().getBettingCoin()
				+ bettingMessageRequest.getBettingCoin());

			// 최소로 배팅해야 하는 코인 업데이트
			// minCoin = bettingMessageRequest.getBettingCoin();

		} else if (bettingMessageRequest.getAction().equals("DIE")) {
			memberManager.getMemberGameInfo().setIsState("DIE");
			// todo (환) 배팅 가능한 라운드 최대 칩 변경 가능성
			// 포기한 사람이 가진 칩의 개수가 라운드에서 가장 적은 사람이었다면 배팅 가능한 칩의 개수가 늘어나야 한다.
			memberManagerMap
				.values()
				.stream()
				.filter(memberManager1 -> memberManager1.getMemberGameInfo().getIsState().equals("BET"))
				.map(MemberManager::getMemberGameInfo)
				.map(MemberGameInfo::getHaveCoin);

		}
	}

	/**
	 * DIE 상태가 아닌 모든 플레이어가 동일한 금액을 베팅했으면 해당 라운드는 종료되어야 한다.
	 */
	public boolean checkDone() {
		// todo (환) 라운드 종료조건 로직 다시보기
		/*
		1. 다이를 외친 사람을 제외한 나머지의 베팅칩이 모두 같으면 라운드 종료
		 */
		List<MemberManager> notDie = memberManagerMap.values()
			.stream()
			.filter(memberManager -> !memberManager.getMemberGameInfo().getIsState().equals("DIE"))
			.toList();

		Integer bettingCoin = notDie.get(0).getMemberGameInfo().getBettingCoin();
		for (MemberManager manager : notDie) {
			if (!bettingCoin.equals(manager.getMemberGameInfo().getBettingCoin()))
				return false;
		}

		return true;
	}

	/**
	 * 라운드가 종료되었을 때, 승리한 사람에게 베팅된 모든 코인 주기
	 * + 라운드가 종료되었을 때, 10을 들고 포기한 사람에게 10개의 코인 몰수하기
	 */
	public void roundEnd() {
		// 승리한 사람 -> 포기하지 않은 사람 중에 카드가 가장 높은 사람
		List<MemberManager> notDie = memberManagerMap.values()
			.stream()
			.filter(memberManager -> !memberManager.getMemberGameInfo().getIsState().equals("DIE"))
			.toList();

		String winner = "";
		int cardNum = 0;

		// 승리한 사람과 카드 넘버 저장
		for (MemberManager manager : notDie) {
			if (manager.getMemberGameInfo().getCardInfo().getCardNumber() > cardNum) {
				winner = manager.getMemberInfo().getNickname();
				cardNum = manager.getMemberGameInfo().getCardInfo().getCardNumber();
			}
		}

		// 승리한 사람에게 베팅한 모든 코인 더해주기
		int getCoin = 0;
		Collection<MemberManager> values = memberManagerMap.values();
		for (MemberManager manager : values) {
			getCoin += manager.getMemberGameInfo().getBettingCoin();
		}
		MemberManager winnerManager = memberManagerMap.get(winner);
		winnerManager.getMemberGameInfo().setHaveCoin(winnerManager.getMemberGameInfo().getHaveCoin() + getCoin);

		// 이긴 사람이 다음 라운드 배팅 턴 잡기
		turnNumber = order.indexOf(winner);
	}

	public void newRoundSetting(int prevRound) {
		currentRound = prevRound + 1;
		// 라운드가 10라운드라면 게임이 끝난다.
		if (currentRound > gameEndRound)
			throw new RuntimeException();

		Collection<MemberManager> memberManagers = memberManagerMap.values();

		long leftMember = memberManagers.stream()
			.filter(memberManager -> memberManager.getMemberGameInfo().getHaveCoin() >= 1)
			.count();

		// 코인이 남은 사람이 1명이면 게임 종료
		if (leftMember == 1)
			throw new RuntimeException();

		for (MemberManager manager : memberManagers) {
			// 코인이 남은 사람만 남은 라운드에 진출할 수 있다.
			if (manager.getMemberGameInfo().getHaveCoin() >= 1) {
				leftMember++;
				// 카드를 받는다.
				manager.getMemberGameInfo().setCardInfo(cardStack.pop());
				// 한 개의 기본베팅을 한다.
				manager.getMemberGameInfo().setHaveCoin(manager.getMemberGameInfo().getHaveCoin() - 1);
				// 베팅 코인이 1개가 된다.
				manager.getMemberGameInfo().setBettingCoin(1);
			}
		}
	}

	public void playReady(String nickname, Boolean isReady) {
		memberManagerMap.get(nickname)
			.getMemberInfo()
			.setIsReady(isReady);
	}

	/**
	 * 대기방에 인원추가
	 */
	public void insertMember(String nickname, Boolean isRoomManager) {
		memberManagerMap.put(nickname,
			MemberManager.create(MemberInfo.create(nickname, isRoomManager), MemberGameInfo.create()));
	}

	/**
	 * 대기방에 인원나감
	 */
	public void deleteMember(String nickname) {
		memberManagerMap.remove(nickname);
	}

	/**
	 * 모든 인원이 레디상태인지 확인한다
	 * 단 방장은 제외한다.
	 */
	private boolean isAllReady() {
		long readyCount = memberManagerMap.values()
			.stream()
			.filter(memberManager -> memberManager.getMemberInfo().getIsReady().equals(Boolean.TRUE))
			.count();

		return readyCount == memberManagerMap.values().size() - 1;
	}

	public void banMember(String banMemberNickname) {
		deleteMember(banMemberNickname);
	}
}

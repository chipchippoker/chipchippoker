package com.chipchippoker.backend.websocket.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

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
	private Stack<CardInfo> cardStack;
	private List<String> order;
	private Integer turnNumber;
	private String roomManager;
	private Integer lastBettingMaxCoin;
	private Integer maxCoin;
	private List<PenaltyInfo> penaltyInfos;

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
		this.lastBettingMaxCoin = 1;
		this.maxCoin = 1;
		this.penaltyInfos = new ArrayList<>();
		log.info("게임방을 생성했습니다.");
	}

	public void nextTurn() {
		for (int i = 0; i < 5; i++) {
			// 다음 사람이 배팅중이라면 턴을 넘겨준다.
			if (memberManagerMap.get(order.get((turnNumber + 1) % memberManagerMap.size()))
				.getMemberGameInfo()
				.getIsState()
				.equals("BET")) {
				setTurnNumber((getTurnNumber() + 1) % memberManagerMap.size());
				return;
			} else {
				turnNumber = (turnNumber + 1) % memberManagerMap.size();
			}
		}
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
			newRoundSetting(0);
		}
	}

	public void betting(BettingMessageRequest bettingMessageRequest, MemberManager memberManager) {
		if (memberManager.getMemberGameInfo().getIsState().equals("DIE")) {
			throw new InvalidException(MessageBase.E400_CAN_NOT_BET_ALREADY_DIE);
		}

		if (bettingMessageRequest.getAction().equals("BET")) {
			if (bettingMessageRequest.getBettingCoin() > memberManager.getMemberGameInfo().getHaveCoin()
				|| bettingMessageRequest.getBettingCoin() < 0
				|| (bettingMessageRequest.getBettingCoin() + memberManager.getMemberGameInfo().getBettingCoin()
				- lastBettingMaxCoin) < 0
				||
				(maxCoin - bettingMessageRequest.getBettingCoin() - memberManager.getMemberGameInfo().getBettingCoin())
					< 0
			) {
				throw new InvalidException(MessageBase.E400_CAN_NOT_BET_BET_COIN_MISMATCH);
			}
			memberManager.getMemberGameInfo().setIsState("BET");

			memberManager.getMemberGameInfo()
				.setHaveCoin(memberManager.getMemberGameInfo().getHaveCoin() - bettingMessageRequest.getBettingCoin());
			memberManager.getMemberGameInfo().setBettingCoin(memberManager.getMemberGameInfo().getBettingCoin()
				+ bettingMessageRequest.getBettingCoin());

			memberManager.getMemberGameInfo().setActionCount(memberManager.getMemberGameInfo().getActionCount() + 1);

			// 최소로 배팅해야 하는 코인 업데이트
			// 본인이 베팅해야하는 최소 베팅금액은 마지막으로 베팅했던 사람의 베팅금액에서 자신이 베팅했던 금액을 뺀 것이다.
			lastBettingMaxCoin = memberManager.getMemberGameInfo().getBettingCoin();

		} else if (bettingMessageRequest.getAction().equals("DIE")) {
			memberManager.getMemberGameInfo().setIsState("DIE");

			List<MemberGameInfo> memberGameInfosInBet = memberManagerMap
				.values()
				.stream()
				.map(MemberManager::getMemberGameInfo)
				.filter(memberGameInfo -> memberGameInfo.getIsState().equals("BET"))
				.toList();

			for (MemberGameInfo memberGameInfo : memberGameInfosInBet) {
				maxCoin = Math.min(maxCoin, memberGameInfo.getBettingCoin() + memberGameInfo.getHaveCoin());
			}
		}
	}

	/**
	 * DIE 상태가 아닌 모든 플레이어가 동일한 금액을 베팅했으면 해당 라운드는 종료되어야 한다.
	 */
	public boolean checkRoundEnd() {
		List<MemberManager> notDie = memberManagerMap.values()
			.stream()
			.filter(memberManager -> !memberManager.getMemberGameInfo().getIsState().equals("DIE"))
			.toList();

		if (notDie.size() == 1)
			return true;

		Integer bettingCoin = notDie.get(0).getMemberGameInfo().getBettingCoin();
		for (MemberManager manager : notDie) {
			if (manager.getMemberGameInfo().getActionCount().equals(0))
				return false;
			if (!bettingCoin.equals(manager.getMemberGameInfo().getBettingCoin()))
				return false;
		}

		return true;
	}

	/**
	 * 라운드가 종료되었을 때, 승리한 사람에게 베팅된 모든 코인 주기
	 * + 라운드가 종료되었을 때, 10을 들고 포기한 사람에게 10개의 코인 몰수하기
	 */
	public String roundEnd() {
		// todo 게임방에 패널티 받은 사람들 닉네임 알려주기, 라운드 종료 메시지에 패널티 정보 추가하기
		List<MemberManager> notDie = memberManagerMap.values()
			.stream()
			.filter(memberManager -> !memberManager.getMemberGameInfo().getIsState().equals("DIE"))
			.toList();

		String winner = "";
		int cardNum = 0;
		int cardSet = 0;

		// 승리한 사람과 카드 넘버 저장
		for (MemberManager manager : notDie) {
			if (manager.getMemberGameInfo().getCardInfo().getCardNumber() > cardNum) {
				winner = manager.getMemberInfo().getNickname();
				cardNum = manager.getMemberGameInfo().getCardInfo().getCardNumber();
				cardSet = manager.getMemberGameInfo().getCardInfo().getCardSet();
			} else if (manager.getMemberGameInfo().getCardInfo().getCardNumber() == cardNum
				&& manager.getMemberGameInfo().getCardInfo().getCardSet() > cardSet) {
				winner = manager.getMemberInfo().getNickname();
				cardNum = manager.getMemberGameInfo().getCardInfo().getCardNumber();
				cardSet = manager.getMemberGameInfo().getCardInfo().getCardSet();
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

		// 다이한 사람들 중 10을 들고 있던 사람들에게서 칩 10개를 몰수해서 우승자에게 주기
		// 10을 들고 포기한 사람들의 MemberManager 가져오기
		List<MemberManager> dieHaveTen = memberManagerMap.values()
			.stream()
			.filter(memberManager -> memberManager.getMemberGameInfo().getIsState().equals("DIE"))
			.filter(memberManager -> memberManager.getMemberGameInfo().getCardInfo().getCardNumber().equals(10))
			.toList();

		if (!dieHaveTen.isEmpty()) {
			for (MemberManager manager : dieHaveTen) {
				if (manager.getMemberGameInfo().getHaveCoin() > 10) {
					penaltyInfos.add(PenaltyInfo.create(
						manager.getMemberInfo().getNickname(),
						10
					));
					winnerManager.getMemberGameInfo().setHaveCoin(
						winnerManager.getMemberGameInfo().getHaveCoin() + 10
					);
					manager.getMemberGameInfo().setHaveCoin(
						manager.getMemberGameInfo().getHaveCoin() - 10
					);
				} else {
					penaltyInfos.add(PenaltyInfo.create(
						manager.getMemberInfo().getNickname(),
						manager.getMemberGameInfo().getHaveCoin(
						)));
					winnerManager.getMemberGameInfo().setHaveCoin(
						winnerManager.getMemberGameInfo().getHaveCoin() + manager.getMemberGameInfo().getHaveCoin()
					);
					manager.getMemberGameInfo().setHaveCoin(0);
				}
			}
		}

		// 이긴 사람이 다음 라운드 배팅 턴 잡기
		turnNumber = order.indexOf(winner);
		return winner;
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

		lastBettingMaxCoin = 1;
		maxCoin = Integer.MAX_VALUE;
		penaltyInfos = new ArrayList<>();

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
				// 베팅중인 상태로 전환한다.
				manager.getMemberGameInfo().setIsState("BET");
				// action 은 0으로 초기화해준다.
				manager.getMemberGameInfo().setActionCount(0);

				// maxCoin 변경
				maxCoin = Math.min(maxCoin, manager.getMemberGameInfo().getHaveCoin() + 1);
			} else {
				manager.getMemberGameInfo().setHaveCoin(0);
				manager.getMemberGameInfo().setBettingCoin(0);
				manager.getMemberGameInfo().setIsState("DIE");
				manager.getMemberGameInfo().setActionCount(0);
				manager.getMemberGameInfo().setCardInfo(new CardInfo(0, 0));
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

	public List<MemberManager> getMemberManagersByCreatedAt() {
		return this.memberManagerMap.values()
			.stream()
			.sorted(Comparator.comparing(MemberManager::getCreatedAt))
			.toList();
	}
}

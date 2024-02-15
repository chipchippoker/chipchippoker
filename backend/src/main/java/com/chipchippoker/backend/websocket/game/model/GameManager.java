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
	private Integer leftCount;
	private Integer leftMemberHaveCoin;
	private Integer leftMemberBettingCoin;

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
		this.leftCount = 0;
		this.leftMemberHaveCoin = 0;
		this.leftMemberBettingCoin = 0;
		log.info("게임방을 생성했습니다.");
	}

	public void nextTurn() {
		for (int i = 0; i < 5; i++) {
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

		Stack<CardInfo> stack = new Stack<>();
		Collections.shuffle(numbers, new Random());
		for (CardInfo cardInfo : numbers) {
			stack.push(cardInfo);
		}
		return stack;
	}

	public void gameStart() {
		if (this.gameState.equals("진행"))
			throw new InvalidException(MessageBase.E400_CAN_NOT_START_ALREADY_START);

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
			.filter(memberManager -> memberManager.getMemberGameInfo().getIsState().equals("BET"))
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
		List<MemberManager> notDie = memberManagerMap.values()
			.stream()
			.filter(memberManager -> memberManager.getMemberGameInfo().getIsState().equals("BET"))
			.toList();

		String winner = "";
		int cardNum = 0;
		int cardSet = 0;

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

		int getCoin = leftMemberBettingCoin;
		Collection<MemberManager> notLeft = memberManagerMap.values()
			.stream()
			.filter(memberManager -> !memberManager.getMemberGameInfo().getIsState().equals("FOLD"))
			.toList();
		for (MemberManager manager : notLeft) {
			manager.getMemberGameInfo()
				.setHaveCoin(
					manager.getMemberGameInfo().getHaveCoin() + leftMemberHaveCoin / (memberManagerMap.size()
						- leftCount));
			getCoin += manager.getMemberGameInfo().getBettingCoin();
		}
		MemberManager winnerManager = memberManagerMap.get(winner);
		winnerManager.getMemberGameInfo().setHaveCoin(winnerManager.getMemberGameInfo().getHaveCoin() + getCoin);

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

		turnNumber = order.indexOf(winner);
		return winner;
	}

	public void newRoundSetting(int prevRound) {
		currentRound = prevRound + 1;
		if (currentRound > gameEndRound)
			throw new RuntimeException();

		Collection<MemberManager> memberManagers = memberManagerMap.values();

		long leftMember = memberManagers.stream()
			.filter(memberManager -> memberManager.getMemberGameInfo().getHaveCoin() >= 1)
			.count();

		if (leftMember == 1)
			throw new RuntimeException();

		lastBettingMaxCoin = 1;
		maxCoin = Integer.MAX_VALUE;
		penaltyInfos = new ArrayList<>();
		leftMemberHaveCoin = 0;
		leftMemberBettingCoin = 0;

		for (MemberManager manager : memberManagers) {
			if (manager.getMemberGameInfo().getHaveCoin() >= 1) {
				leftMember++;
				manager.getMemberGameInfo().setCardInfo(cardStack.pop());
				manager.getMemberGameInfo().setHaveCoin(manager.getMemberGameInfo().getHaveCoin() - 1);
				manager.getMemberGameInfo().setBettingCoin(1);
				manager.getMemberGameInfo().setIsState("BET");
				manager.getMemberGameInfo().setActionCount(0);

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
	 * 게임방 중간에 나감
	 */
	public void deleteMemberInGameRoom(String nickname) {
		MemberManager manager = memberManagerMap.get(nickname);
		this.leftCount += 1;
		this.leftMemberHaveCoin += manager.getMemberGameInfo().getHaveCoin();
		this.leftMemberBettingCoin += manager.getMemberGameInfo().getBettingCoin();
		manager.getMemberGameInfo().setIsState("FOLD");
		manager.getMemberGameInfo().setHaveCoin(0);
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

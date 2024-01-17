package com.chipchippoker.backend.websocket.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

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
	private Map<Integer, RoundManager> roundManagerMap;
	private Map<String, MemberManager> memberManagerMap;
	private Integer currentRound;
	private final Integer gameEndRound = 10;
	private final Integer bettingLimitTime = 15;
	private Stack<Integer> cardStack;
	private List<String> order;
	private Integer turnNumber;
	private Integer isDone;
	private String roomManager;

	public GameManager(String roomTitle, Integer countOfPeople, String nickname) {
		log.info("게임방을 생성합니다.");
		this.roomTitle = roomTitle;
		this.gameState = "대기";
		this.countOfPeople = countOfPeople;
		this.roundManagerMap = new HashMap<>();
		this.memberManagerMap = new HashMap<>();
		this.currentRound = 1;
		this.cardStack = makeCardStack(countOfPeople);
		this.order = new ArrayList<>();
		this.turnNumber = 0;
		this.isDone = 0;
		this.roomManager = nickname;
		log.info("게임방을 생성했습니다.");
	}

	public boolean nextTurn() {
		for (int i = 0; i < 5; i++) {
			// 다음 사람이 배팅중이라면 턴을 넘겨준다.
			if (memberManagerMap.get(order.get((turnNumber + 1) % countOfPeople)).getIsState().equals("BET")) {
				setTurnNumber((getTurnNumber() + 1) % getCountOfPeople());
				return true;
				// 다음 사람이 베팅중이 아니라면 그 다음 사람에게 턴을 넘겨줘야한다.
			} else {
				turnNumber = (turnNumber + 1) % countOfPeople;
			}
		}
		return false;
	}

	private Stack<Integer> makeCardStack(int countOfPeople) {
		List<Integer> numbers = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			numbers.add(i);
		}

		// Stack 생성
		Stack<Integer> stack = new Stack<>();

		// 각 세트에 대해 랜덤으로 숫자를 섞어서 Stack에 삽입
		for (int set = 1; set <= countOfPeople; set++) {
			// 리스트를 섞기
			Collections.shuffle(numbers, new Random());

			// 리스트를 Stack에 삽입
			for (int num : numbers) {
				stack.push(num);
			}
		}

		return stack;
	}
}

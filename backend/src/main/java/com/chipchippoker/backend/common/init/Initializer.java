package com.chipchippoker.backend.common.init;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;
import com.chipchippoker.backend.common.collection.GameResult;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
@Profile({"dev", "local"})
public class Initializer implements ApplicationRunner {
	private final GameResultRepository gameResultRepository;
	private final MongoTemplate mongoTemplate;

	public void dropCollection(String collectionName) {
		if (mongoTemplate.collectionExists(collectionName)) {
			mongoTemplate.dropCollection(collectionName);
		}
	}

	@Override
	public void run(ApplicationArguments args) {
		dropCollection("game_result");

		List<String> memberList1 = makeMemberList("임세환닉네임", "권순준닉네임", "선수연닉네임");
		List<String> memberList2 = makeMemberList("최현기닉네임", "권순준닉네임", "선수연닉네임", "윤예빈닉네임");
		List<String> memberList3 = makeMemberList("윤예빈닉네임", "최현기닉네임", "김대원닉네임", "선수연닉네임");
		List<String> memberList4 = makeMemberList("선수연닉네임", "김대원닉네임");
		List<String> memberList5 = makeMemberList("김대원닉네임", "윤예빈닉네임", "선수연닉네임");
		List<String> memberList6 = makeMemberList("권순준닉네임", "최현기닉네임", "임세환닉네임");
		List<String> memberList7 = makeMemberList("최현기닉네임", "김대원닉네임", "선수연닉네임", "권순준닉네임");
		List<String> memberList8 = makeMemberList("권순준닉네임", "임세환닉네임", "김대원닉네임", "선수연닉네임");
		List<String> memberList9 = makeMemberList("김대원닉네임", "최현기닉네임", "임세환닉네임", "윤예빈닉네임");
		List<String> memberList10 = makeMemberList("권순준닉네임", "선수연닉네임", "임세환닉네임");
		List<String> memberList11 = makeMemberList("윤예빈닉네임", "최현기닉네임", "임세환닉네임");
		List<String> memberList12 = makeMemberList("권순준닉네임", "윤예빈닉네임", "임세환닉네임");
		List<String> memberList13 = makeMemberList("권순준닉네임", "김대원닉네임", "윤예빈닉네임");
		List<String> memberList14 = makeMemberList("윤예빈닉네임", "최현기닉네임", "임세환닉네임");
		List<String> memberList15 = makeMemberList("권순준닉네임", "최현기닉네임", "임세환닉네임");
		List<List<String>> memberList = Arrays.asList(memberList1, memberList2, memberList3, memberList4, memberList5,
			memberList6, memberList7, memberList8, memberList9, memberList10, memberList11, memberList12, memberList13,
			memberList14, memberList15);

		List<Integer> resultCoinList1 = makeCoinList(10, 25, 40);
		List<Integer> resultCoinList2 = makeCoinList(30, 30, 15, 25);
		List<Integer> resultCoinList3 = makeCoinList(20, 20, 0, 60);
		List<Integer> resultCoinList4 = makeCoinList(20, 30);
		List<Integer> resultCoinList5 = makeCoinList(75, 0, 0);
		List<Integer> resultCoinList6 = makeCoinList(24, 26, 25);
		List<Integer> resultCoinList7 = makeCoinList(24, 26, 25, 25);
		List<Integer> resultCoinList8 = makeCoinList(30, 20, 10, 40);
		List<Integer> resultCoinList9 = makeCoinList(10, 20, 30, 40);
		List<Integer> resultCoinList10 = makeCoinList(12, 24, 39);
		List<Integer> resultCoinList11 = makeCoinList(0, 10, 65);
		List<Integer> resultCoinList12 = makeCoinList(19, 28, 38);
		List<Integer> resultCoinList13 = makeCoinList(30, 30, 15);
		List<Integer> resultCoinList14 = makeCoinList(5, 8, 62);
		List<Integer> resultCoinList15 = makeCoinList(24, 26, 25);
		List<List<Integer>> resultCoinList = Arrays.asList(resultCoinList1, resultCoinList2, resultCoinList3,
			resultCoinList4, resultCoinList5,
			resultCoinList6, resultCoinList7, resultCoinList8, resultCoinList9, resultCoinList10, resultCoinList11,
			resultCoinList12, resultCoinList13, resultCoinList14, resultCoinList15);

		for (int i = 0; i < 15; i++) {
			GameResult gameResult = GameResult.newGameResult(memberList.get(i), resultCoinList.get(i),
				Long.parseLong(i + ""), i % 2 == 0 ? "경쟁" : "친선");
			gameResultRepository.save(gameResult);
		}

	}

	public List<String> makeMemberList(String... names) {
		return Arrays.asList(names);
	}

	public List<Integer> makeCoinList(Integer... coins) {
		return Arrays.asList(coins);
	}
}


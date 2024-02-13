package com.chipchippoker.backend.api.gameresult.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.chipchippoker.backend.common.collection.GameResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameResultRepositoryCustomImpl implements GameResultRepositoryCustom {
	private final MongoTemplate mongoTemplate;

	public List<GameResult> findRecentPlayList(String nickname) {
		List<GameResult> recentPlayList = mongoTemplate.find(
			Query.query(Criteria.where("memberList").elemMatch(Criteria.where("$eq").is(nickname)))
				.with(Sort.by(Sort.Order.desc("createdAt"))).limit(10),
			GameResult.class);
		return recentPlayList;
	}
}

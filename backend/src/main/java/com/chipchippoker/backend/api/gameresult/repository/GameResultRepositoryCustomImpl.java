package com.chipchippoker.backend.api.gameresult.repository;

import com.chipchippoker.backend.common.collection.GameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class GameResultRepositoryCustomImpl implements GameResultRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public Integer getCoin() {
        GameResult gameResult = mongoTemplate.findOne(
                Query.query(Criteria.where("memberAId").is(1)), GameResult.class
        );
        return gameResult.getMemberACoin();
    }
}

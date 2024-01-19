package com.chipchippoker.backend.api.gameresult.repository;

import java.util.List;

import com.chipchippoker.backend.common.collection.GameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@RequiredArgsConstructor
public class GameResultRepositoryCustomImpl implements GameResultRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    // @Override
    // public Integer getCoin() {
    //     GameResult gameResult = mongoTemplate.findOne(
    //             Query.query(Criteria.where("memberAId").is(123456789)), GameResult.class
    //     );
    //     return gameResult.getMemberACoin();
    // }

    public List<GameResult> findRecentPlayList(String nickname) {
        List<GameResult> recentPlayList = mongoTemplate.find(
            Query.query(Criteria.where("memberList").elemMatch(Criteria.where("$eq").is(nickname))).limit(10),
            GameResult.class);
        return recentPlayList;
    }

    // public List<GameResult> findRecentPlayList(){
    //     List<GameResult> recentPlayList = mongoTemplate.find(Query.query(Criteria.where("memberAId").is()))
    // }
}

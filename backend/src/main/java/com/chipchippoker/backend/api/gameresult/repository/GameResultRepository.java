package com.chipchippoker.backend.api.gameresult.repository;

import com.chipchippoker.backend.common.collection.GameResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends MongoRepository<GameResult, String>, GameResultRepositoryCustom {
}

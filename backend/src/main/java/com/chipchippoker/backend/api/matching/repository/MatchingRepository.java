package com.chipchippoker.backend.api.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingRepositoryCustom {
}

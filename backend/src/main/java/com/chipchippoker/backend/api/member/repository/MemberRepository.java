package com.chipchippoker.backend.api.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	Optional<Member> findByNickname(String nickname);
}

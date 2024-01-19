package com.chipchippoker.backend.api.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	Optional<Member> findByNickname(String nickname);

	Boolean existsByMemberId(String id);

	Boolean existsByNickname(String nickname);

	Boolean existsByKakaoSocialId(Long kakaoSocialId);

	Optional<Member> findByMemberId(String memberId);

	Optional<Member> findByKakaoSocialId(Long socialId);

}

package com.chipchippoker.backend.api.friend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
	Friend findByMemberAIdAndMemberBId(Long memberAId, Long memberBId);

	List<Friend> findByMemberA(Member memberA);
}

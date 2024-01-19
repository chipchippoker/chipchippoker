package com.chipchippoker.backend.api.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
	Friend findByMemberAIdAndMemberBId(Long memberAId, Long memberBId);
}

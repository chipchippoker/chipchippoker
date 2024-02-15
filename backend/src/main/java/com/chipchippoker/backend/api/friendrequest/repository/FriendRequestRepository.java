package com.chipchippoker.backend.api.friendrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chipchippoker.backend.common.entity.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long>, FriendRequestRepositoryCustom {
	FriendRequest findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}

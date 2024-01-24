package com.chipchippoker.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching extends BaseEntity {
	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "total_participant_cnt", nullable = false)
	private Integer totalParticipantCnt;

	@Column(name = "is_waiting", nullable = false)
	private Boolean isWaiting = Boolean.FALSE;
}
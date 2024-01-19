package com.chipchippoker.backend.common.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend extends BaseEntity {
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "member_a_id", nullable = false)
	private Member memberA;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "member_b_id", nullable = false)
	private Member memberB;

	public static Friend createFriend(Member memberA, Member memberB) {
		return Friend.builder()
			.memberA(memberA)
			.memberB(memberB)
			.build();
	}
}

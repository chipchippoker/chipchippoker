package com.chipchippoker.backend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Point extends BaseEntity {
	@Column(name = "win")
	private Integer win;
	@Column(name = "draw")
	private Integer draw;
	@Column(name = "lose")
	private Integer lose;
	@Column(name = "current_winning_streak")
	private Integer currentWinningStreak;
	@Column(name = "max_win")
	private Integer maxWin;
	@Column(name = "point_score")
	private Integer pointScore;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public static Point createPoint(Member member) {
		return Point.builder()
			.win(0)
			.draw(0)
			.lose(0)
			.currentWinningStreak(0)
			.maxWin(0)
			.pointScore(1000)
			.member(member)
			.build();
	}

	public static String tierByPoint(Integer pointScore){
		if(pointScore<800){
			return "bronze";
		}else if(pointScore<1200){
			return "silver";
		}else if(pointScore<1600){
			return "gold";
		}else if(pointScore<2000){
			return "platinum";
		}else{
			return "diamond";
		}
	}

	public static Integer calculatePoint(Integer coin){
		Integer initialCoin = 25;
		Integer base = coin/initialCoin;
		Integer point = 0;
		point+= base > 0 ? base * 10 : (base - 1) * 10;
		point+= coin-initialCoin;
		return point;
	}
}

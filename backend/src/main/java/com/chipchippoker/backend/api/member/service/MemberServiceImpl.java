package com.chipchippoker.backend.api.member.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chipchippoker.backend.api.gameresult.repository.GameResultRepository;
import com.chipchippoker.backend.api.member.dto.model.ProfilePageResponse;
import com.chipchippoker.backend.api.member.dto.model.RecentPlayListResponse;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.collection.GameResult;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final GameResultRepository gameResultRepository;
	@Override
	public ProfilePageResponse getProfilePage(Long id, String nickname) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		//해당 닉네임의 사용자가 없으면 에러
		memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));
		//현재 확인하는 프로필 페이지가 내 페이지인지 확인
		boolean isMine = member.getNickname().equals(nickname);

		//최근 10게임의 대한 정보를 가져오는 리스트
		List<GameResult> list = gameResultRepository.findRecentPlayList(nickname);
		//최근 10개임의 정보를 원하는 응답값으로 매핑하는 메소드
		List<RecentPlayListResponse> recentPlayList = getRecentPlayList(list,nickname);

		//todo 전체 랭킹, 친구 랭킹 추가
		return memberRepository.getProfilePage(member,isMine,recentPlayList);

	}

	private static List<RecentPlayListResponse> getRecentPlayList(List<GameResult> list,String nickname) {
		List<RecentPlayListResponse> recentPlayList = new ArrayList<>();
		// 최근 10게임 리스트
		for (GameResult gameResult : list) {
			Map<String,String> players = new HashMap<>();;
			//해당 게임에서 바뀐 점수
			int changePoint = 0;
			//게임당 결과
			for(int i=0;i<gameResult.getMemberList().size();i++){
				if(gameResult.getMemberList().get(i).equals(nickname)){
					changePoint = Point.calculatePoint(gameResult.getResultCoinList().get(i));
				}
				int resultCoin = gameResult.getResultCoinList().get(i);
				String outcome = resultCoin>25?"win":(resultCoin==25?"draw":"lose");
				players.put(gameResult.getMemberList().get(i),outcome);
			}
			RecentPlayListResponse recentPlayListResponse = RecentPlayListResponse.createRecentPlayListResponse(
				players, gameResult.getGameMode(), gameResult.getMemberList().size(), changePoint);
			recentPlayList.add(recentPlayListResponse);
		}
		return recentPlayList;
	}
}

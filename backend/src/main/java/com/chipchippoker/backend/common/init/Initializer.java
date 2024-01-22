package com.chipchippoker.backend.common.init;


import org.hibernate.annotations.processing.SQL;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.api.auth.model.dto.request.SignupRequest;
import com.chipchippoker.backend.api.friend.repository.FriendRepository;
import com.chipchippoker.backend.api.member.repository.MemberRepository;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.entity.Friend;
import com.chipchippoker.backend.common.entity.Member;
import com.chipchippoker.backend.common.entity.Point;
import com.chipchippoker.backend.common.exception.NotFoundException;
import com.querydsl.core.annotations.QueryInit;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class Initializer implements ApplicationRunner {
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;
	private final EntityManager entityManager;

	@Override
	public void run(ApplicationArguments args) {
		// 더미 데이터 작성

		String[] names = {"최현기","권순준","임세환","선수연","윤예빈","김대원","싸피1","대원이","김현기","김지수","윤동희","권희주","선주리","임주호","윤희서","김명주","김희주","싸피2","싸피3","싸피4","서울4반","서울5반","서울8반","인천","대구","대전","부산","울산","김김","님님","딤딤","림림"};
		String[] nicknames = {"최현기","권순준","임세환","선수연","윤예빈","김대원","싸피1","대원이","김현기","김지수","윤동희","권희주","선주리","임주호","윤희서","김명주","김희주","싸피2","싸피3","싸피4","서울4반","서울5반","서울8반","인천","대구","대전","부산","울산","김김","님님","딤딤","림림"};

		for(int i=0;i<names.length;i++){
			SignupRequest signupRequest = new SignupRequest(names[i],"asd","asd",nicknames[i],"asd");
			Member member = Member.newMember(signupRequest);
			Point point = Point.createPoint(member,1000+i*100);
			member.createPoint(point);
			memberRepository.save(member);
		}
		Member member = memberRepository.findById(1L)
			.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

		entityManager.flush();
		for(int i=2;i<15;i++){
			Member friend = memberRepository.findById((long)i)
				.orElseThrow(() -> new NotFoundException(ErrorBase.E404_NOT_EXISTS_MEMBER));

			Friend fromMemberFriend = Friend.createFriend(member, friend);
			friendRepository.save(fromMemberFriend);
			Friend toMemberFriend = Friend.createFriend(friend, member);
			friendRepository.save(toMemberFriend);
		}

	}
}


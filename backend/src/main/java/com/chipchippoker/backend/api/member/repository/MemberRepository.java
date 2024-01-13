
package com.chipchippoker.backend.api.member.repository;

import com.chipchippoker.backend.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
}

package com.gregori.repository.member;

import com.gregori.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long memberId);
}

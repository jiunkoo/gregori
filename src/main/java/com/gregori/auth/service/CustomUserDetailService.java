package com.gregori.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final MemberMapper memberMapper;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) {

		Member member = memberMapper.findByEmail(email).orElseThrow(NotFoundException::new);
		if (member.getStatus() == DEACTIVATE) {
			throw new AccessDeniedException("탈퇴한 사용자입니다.");
		}

		return createUserDetails(member);
	}

	private UserDetails createUserDetails(Member member) {

		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(grantedAuthority);

		return new User(String.valueOf(member.getId()), member.getPassword(), authorities);
	}
}

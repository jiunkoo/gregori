package com.gregori.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {
	MEMBER("회원"), ADMIN("관리자");
	private final String description;
}

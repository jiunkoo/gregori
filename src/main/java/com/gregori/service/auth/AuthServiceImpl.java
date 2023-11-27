package com.gregori.service.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.domain.auth.RefreshToken;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.dto.auth.TokenDto;
import com.gregori.dto.auth.TokenRequestDto;
import com.gregori.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	@Transactional
	public TokenDto signIn(AuthSignInDto authSignInDto) {
		UsernamePasswordAuthenticationToken authenticationToken = authSignInDto.toAuthentication();
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.generateToken(authentication);
		RefreshToken refreshToken = refreshTokenMapper.findByRtKey(authentication.getName()).orElse(null);
		if (refreshToken != null) {
			refreshTokenMapper.delete(refreshToken.getId());
		}

		refreshTokenMapper.insert(authSignInDto.toEntity(authentication.getName(), tokenDto.getRefreshToken()));

		return tokenDto;
	}

	@Override
	@Transactional
	public Long signOut(TokenRequestDto tokenRequestDto) {
		RefreshToken refreshToken = getRefreshToken(tokenRequestDto, getAuthentication(tokenRequestDto));

		return refreshTokenMapper.delete(refreshToken.getId());
	}

	@Override
	@Transactional
	public TokenDto refresh(TokenRequestDto tokenRequestDto) {
		Authentication authentication = getAuthentication(tokenRequestDto);
		TokenDto tokenDto = tokenProvider.generateToken(authentication);
		RefreshToken refreshToken = getRefreshToken(tokenRequestDto, authentication);
		refreshToken.updateValue(tokenDto.getRefreshToken());
		refreshTokenMapper.update(refreshToken);

		return tokenDto;
  	}

	private RefreshToken getRefreshToken(TokenRequestDto tokenRequestDto, Authentication authentication) {
		RefreshToken refreshToken = refreshTokenMapper.findByRtKey(authentication.getName())
			.orElseThrow(() -> new UnauthorizedException("로그아웃한 사용자입니다."));

		if (!refreshToken.getRtValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new UnauthorizedException("토큰의 유저 정보가 일치하지 않습니다.");
		}

		return refreshToken;
	}

	private Authentication getAuthentication(TokenRequestDto tokenRequestDto) {
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new ValidationException("Refresh Token이 유효하지 않습니다.");
		}

		return tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
	}
}

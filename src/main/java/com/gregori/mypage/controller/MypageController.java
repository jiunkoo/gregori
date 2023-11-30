package com.gregori.mypage.controller;

import static java.lang.Long.parseLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.common.response.CustomResponse;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<CustomResponse<Long>> updateMember(@RequestBody @Valid MemberUpdateDto mypageUpdateDto) {
		authorizationCheck(mypageUpdateDto.getId());

		CustomResponse<Long> response = CustomResponse
			.success(memberService.updateMember(mypageUpdateDto), "회원 수정에 성공했습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<CustomResponse<Long>> deactivateMember(@PathVariable Long memberId) {
		authorizationCheck(memberId);

		CustomResponse<Long> response = CustomResponse
			.success(memberService.deactivateMember(memberId), "회원 탈퇴에 성공했습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<CustomResponse<MemberResponseDto>> findMemberById(@PathVariable Long memberId) {
		authorizationCheck(memberId);

		CustomResponse<MemberResponseDto> response = CustomResponse
			.success(memberService.findMemberById(memberId), "회원 정보를 가져왔습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private void authorizationCheck(Long memberId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long currentMemberId = parseLong(authentication.getName());
		if (currentMemberId != memberId) {
			throw new AccessDeniedException();
		}
	}
}

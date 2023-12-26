package com.gregori.member.controller;

import java.net.URI;

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.member.dto.MemberPasswordUpdateDto;

import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

import static java.lang.Long.parseLong;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid MemberRegisterDto dto) {

        Long memberId = memberService.register(dto);

        return ResponseEntity.created(URI.create("/member/" + memberId)).build();
    }

    @PostMapping("/name")
    public ResponseEntity<Void> updateMemberName(@RequestBody @Valid MemberNameUpdateDto dto) {

        checkAuthorization(dto.getId());
        memberService.updateMemberName(dto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password")
    public ResponseEntity<Void> updateMemberPassword(@RequestBody @Valid MemberPasswordUpdateDto dto) {

        checkAuthorization(dto.getId());
        memberService.updateMemberPassword(dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {

        checkAuthorization(memberId);

        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberId) {

        checkAuthorization(memberId);

        MemberResponseDto response = memberService.getMember(memberId);

        return ResponseEntity.ok().body(response);
    }

    private void checkAuthorization(Long memberId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long currentMemberId = parseLong(authentication.getName());

        if (currentMemberId != memberId) {
            throw new AccessDeniedException();
        }
    }
}

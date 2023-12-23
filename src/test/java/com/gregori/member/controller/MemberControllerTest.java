package com.gregori.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.service.MemberService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = MemberController.class)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	MemberService memberService;

	MemberRegisterDto memberRegisterDto;

	@Test
	@DisplayName("회원가입을 요청하면 OK 응답을 반환한다.")
	void should_responseCreated_when_requestRegister() throws Exception {

		// given
		memberRegisterDto = new MemberRegisterDto("일호", "a@a.a", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
			.post("/member/register")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberRegisterDto)));

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		verify(memberService).register(refEq(memberRegisterDto));
	}

	@Test
	@DisplayName("회원 이름 수정을 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestUpdateMemberName() throws Exception {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto(1L, "이름");
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/member/name")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(memberService).updateMemberName(refEq(dto));
	}

	@Test
	@DisplayName("회원 비밀번호 수정을 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestUpdateMemberPassword() throws Exception {

		// given
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto(1L, "aa11111!", "aa11111!");
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/member/password")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(memberService).updateMemberPassword(refEq(dto));
	}

	@Test
	@DisplayName("회원 탈퇴를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestDeleteMember() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/member/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(memberService).deleteMember(1L);
	}

	@Test
	@DisplayName("회원 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetMember() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/member/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(memberService).getMember(1L);
	}

	@Test
	@DisplayName("회원 id가 토큰 id와 불일치하면 AccessDeniedException이 발생한다.")
	void should_AccessDeniedException_when_invalidMemberId() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("2");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/member/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isForbidden()).andDo(print());
	}
}

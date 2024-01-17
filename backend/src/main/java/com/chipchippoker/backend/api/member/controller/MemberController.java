package com.chipchippoker.backend.api.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Member API Controller", description = "API 정보를 제공하는 멤버 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}
}


package com.chipchippoker.backend.common.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chipchippoker.backend.common.util.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// preflight는 통과
		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			return true;
		}
		// 카카오 access-token도 통과
		if (request.getHeader("kakao-access-token") != null) {
			return true;
		}
		String accessToken = request.getHeader("access-token");
		String refreshToken = request.getHeader("refresh-token");
		Long id = jwtUtil.getId(accessToken, refreshToken);
		request.setAttribute("id", id);
		return true;
	}
}

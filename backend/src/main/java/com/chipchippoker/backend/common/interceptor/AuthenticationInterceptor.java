package com.chipchippoker.backend.common.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.UnAuthorizedException;
import com.chipchippoker.backend.common.util.jwt.JwtUtil;
import com.chipchippoker.backend.common.util.jwt.dto.TokenResponse;

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
		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			return true;
		}
		if (request.getHeader("kakao-access-token") != null) {
			return true;
		}
		try {
			Long id = jwtUtil.getId(request.getHeader("access-token"),
				request.getHeader("refresh-token"));
			request.setAttribute("id", id);
		} catch (UnAuthorizedException e) {
			//accessToken, refreshToken 재발급
			TokenResponse tokenResponse = jwtUtil.reissuanceTokens(request.getHeader("refresh-token"));
			request.setAttribute("new-token", tokenResponse);
			throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESS_TOKEN);
		} catch (InvalidException e) {
			throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
		}
		return true;
	}
}

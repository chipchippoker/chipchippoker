package com.chipchippoker.backend.api.jwt.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chipchippoker.backend.api.jwt.model.dto.TokenResponse;
import com.chipchippoker.backend.api.jwt.serivce.JwtUtil;
import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			Long id = jwtUtil.getId();
			request.setAttribute("id", id);
		} catch (UnAuthorizedException e) {
			//accessToken, refreshToken 재발급
			TokenResponse tokenResponse = jwtUtil.reissuanceTokens();
			request.setAttribute("new-token", tokenResponse);
			throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESSTOKEN);
		} catch (InvalidException e) {
			throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
		}
		return true;
	}
}

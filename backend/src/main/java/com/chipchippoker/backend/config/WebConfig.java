package com.chipchippoker.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chipchippoker.backend.common.interceptor.AuthenticationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private final AuthenticationInterceptor authenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry reg) {
		reg.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**") //interceptor 작업이 필요한 path를 모두 추가한다.
			.excludePathPatterns("/api/auth/**")
			.excludePathPatterns("/api/members/signup", "/api/members/login")
			.excludePathPatterns("/api/members/duplication/**");
		// 인가작업에서 제외할 API 경로를 따로 추가할수도 있으나, 일일히 따로 추가하기 어려우므로 어노테이션을 따로 만들어 해결한다.
	}
}

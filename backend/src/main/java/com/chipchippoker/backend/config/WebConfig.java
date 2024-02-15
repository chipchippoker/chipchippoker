package com.chipchippoker.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
			.addPathPatterns("/**")
			.excludePathPatterns("/api/auth/**")
			.excludePathPatterns("/actuator/prometheus");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5173", "http://i10a804.p.ssafy.io",
				"https://i10a804.p.ssafy.io", "https://chipchippoker.shop")
			.allowedMethods("*")
			.allowedHeaders("access-token", "refresh-token", "Content-Type", "kakao-access-token")
			.allowCredentials(true)
			.maxAge(3600);

	}
}
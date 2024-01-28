package com.chipchippoker.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.chipchippoker.backend.common.handler.InboundChannelInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final InboundChannelInterceptor inboundChannelInterceptor;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry
			.setApplicationDestinationPrefixes("/to")
			.enableSimpleBroker("/from")
			.setHeartbeatValue(new long[] {10000L, 10000L});
		// .setTaskScheduler(heartBeatScheduler());

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint("/chipchippoker")
			.setAllowedOriginPatterns("*");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(inboundChannelInterceptor);
	}

	@Bean
	public TaskScheduler heartBeatScheduler() {
		return new ThreadPoolTaskScheduler();
	}
}
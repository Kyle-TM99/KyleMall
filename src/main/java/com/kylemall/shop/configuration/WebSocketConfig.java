package com.kylemall.shop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { //메시지 브로커(Message Broker)의 동작을 설정합니다.
        config.enableSimpleBroker("/topic"); // 메시지 브로커가 메시지를 전달할 수 있는 경로를 설정합니다.
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 보낼 수 있는 경로를 설정합니다.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //Stomp 엔드포인트를 등록합니다.
        registry.addEndpoint("/ws-chat") // 웹소켓 엔드포인트를 등록합니다.
                .setAllowedOriginPatterns("*") // 모든 출처에서 연결을 허용합니다.
                .withSockJS(); // SockJS를 사용하여 웹소켓을 지원하는 브라우저에서도 사용할 수 있도록 합니다.
    }
} 
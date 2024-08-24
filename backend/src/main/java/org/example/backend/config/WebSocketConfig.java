package org.example.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * packageName : org.example.backend.config
 * fileName : WebSocketConfig
 * author : kimtaewan
 * date : 2024-05-29
 * description : 웹소켓 설정
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         kimtaewan          최초 생성
 */
@ComponentScan  // 자동 bean 등록 어노테이션
@Configuration
@EnableWebSocketMessageBroker   // STOMP 사용 어노테이션
// STOMP 사용시 implements
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 수발신 엔드포인트
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    // 연결 엔드포인트
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
    }
}
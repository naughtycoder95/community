package org.example.backend.config;

import org.example.backend.model.dto.ChatMessageDto;
import org.example.backend.model.entity.Notify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * packageName : org.example.backend.config
 * fileName : RedisConfig
 * author : kimtaewan
 * date : 2024-05-26
 * description : 레디스 설정
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-26         kimtaewan          최초 생성
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> notifyRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // NotifyDto 직렬화
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Notify.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> chatRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // ChatMessageDto 직렬화
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        return redisTemplate;
    }
}
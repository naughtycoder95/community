package org.example.backend.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.ChatMessageDto;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName : org.example.backend.service.auth
 * fileName : ChatService
 * author : kimtaewan
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         kimtaewan          최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final RedisPubService redisPubService;
    private final ChatSubService chatSubService;
    // 쓰레드 세이프 자료구조 사용 -> 여러 클라이언트에서 접속가능
    private final ConcurrentHashMap<String, ChannelTopic> topics = new ConcurrentHashMap<>();
    private final RedisMessageListenerContainer redisMessageListener;

    public void message(ChatMessageDto message) {
        String channel = "chat";
        // 채널 존재하지 않을 경우 채널 생성
        topics.computeIfAbsent(channel, ch -> {
            ChannelTopic topic = new ChannelTopic(ch);
            redisMessageListener.addMessageListener(chatSubService, topic);
            return topic;
        });
        // 레디스로 메세지 퍼블리싱
        redisPubService.chatPublish(channel, message);
    }
}

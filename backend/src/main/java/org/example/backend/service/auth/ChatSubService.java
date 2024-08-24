package org.example.backend.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.ChatMessageDto;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * packageName : org.example.backend.service.profile
 * fileName : ChatSubService
 * author : kimtaewan
 * date : 2024-05-30
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-30         kimtaewan          최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChatSubService implements MessageListener {

    private final RedisTemplate<String, Object> chatRedisTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 레디스에서 수신한 메세지 역직렬화
        ChatMessageDto chatMessage = (ChatMessageDto) chatRedisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("메세지 퍼블리싱 : {}", chatMessage);
        // 메세지를 웹소켓으로 퍼블리싱
        messagingTemplate.convertAndSend("/topic/message", chatMessage);
    }
}

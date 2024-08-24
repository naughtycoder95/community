package org.example.backend.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.entity.Notify;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * packageName : org.example.backend.service
 * fileName : RedisSubService
 * author : kimtaewan
 * date : 2024-05-26
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-26         kimtaewan          최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotifySubService implements MessageListener {

    private final RedisTemplate<String, Object> notifyRedisTemplate;

    private final SseService sseService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 레디스에서 수신한 메세지 역직렬화
        Notify notify = (Notify) notifyRedisTemplate.getValueSerializer().deserialize(message.getBody());
        sseService.sendSseEvent(notify);
    }
}
package org.example.backend.controller.auth;

import lombok.RequiredArgsConstructor;
import org.example.backend.service.auth.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * packageName : org.example.backend.controller.auth
 * fileName : SseController
 * author : kimtaewan
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         kimtaewan          최초 생성
 */
@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    // 미디어 타입 설정해야 SSE 동작
    @GetMapping(value = "/api/connect/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable String memberId) {
        // 타임아웃 설정
        SseEmitter emitter = new SseEmitter(3600000L);
        sseService.subscribe(emitter, memberId);
        try {
            // 최초 연결시 더미데이터 발송
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }
}

package org.example.backend.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.NotifyDto;
import org.example.backend.service.auth.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.backend.controller.auth
 * fileName : NotifyController
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
@Slf4j
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {
    private final NotifyService notifyService;

    @PostMapping("/best/{boardId}")
    public ResponseEntity<String> createBestNotify(@PathVariable Long boardId, @RequestBody NotifyDto notifyDto) {
        notifyService.createBestNotify(boardId, notifyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("베스트 알림이 성공적으로 생성되었습니다.");
    }

    @PostMapping("/hot-topic/{boardId}")
    public ResponseEntity<String> createHotTopicNotify(@PathVariable Long boardId, @RequestBody NotifyDto notifyDto) {
        notifyService.createHotTopicNotify(boardId, notifyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("핫토픽 알림이 성공적으로 생성되었습니다.");
    }

    @PostMapping("/reply/{boardId}")
    public ResponseEntity<String> createReplyNotify(@PathVariable Long boardId, @RequestBody NotifyDto notifyDto) {
        notifyService.createReplyNotify(boardId, notifyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 알림이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/read-check/{notifyId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notifyId) {
        notifyService.readCheck(notifyId);
        return ResponseEntity.ok("알림을 확인했습니다.");
    }

    @PutMapping("/read-check/all")
    public ResponseEntity<String> markAsReadAll(@RequestBody NotifyDto notifyDto) {
        notifyService.readCheckAll(notifyDto);
        return ResponseEntity.ok("알림을 확인했습니다.");
    }
}
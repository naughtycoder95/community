package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.MainPageArmDto;
import org.example.backend.model.dto.NotifyDto;
import org.example.backend.service.auth.NotifyService;
import org.example.backend.service.board.MainPageNotiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName : org.example.backend.controller.board
 * fileName : MainPageNotiController
 * author : GGG
 * date : 2024-05-24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-24         GGG          최초 생성
 */
@RestController
@Slf4j
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class MainPageNotiController {

    private final MainPageNotiService mainPageNotiService;

    private final NotifyService notifyService;

    // 멤버별 알람 상세 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<MainPageArmDto>> getNotificationsByMemberId(@PathVariable String memberId) {
        List<MainPageArmDto> notifications = mainPageNotiService.getNotificationsByMemberId(memberId);
        return ResponseEntity.ok(notifications);
    }

    // 알람카운트 조회 함수
    @GetMapping("/count/{memberId}")
    public ResponseEntity<Integer> getCountNotification(@PathVariable String memberId) {
        int count = mainPageNotiService.countNotification(memberId);
        return ResponseEntity.ok(count);
    }

//    알람내용 클릭시 NotiCheck Y로 변경
    @PostMapping("/{notifyId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notifyId) {
        notifyService.readCheck(notifyId);
        return ResponseEntity.ok().build(); // 성공 응답
    }

//    알람 하드딜리트 함수
    @DeleteMapping("/{notifyId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notifyId) {
        mainPageNotiService.deleteNotification(notifyId);
        return ResponseEntity.ok().build();
    }

//    모두확인 함수
    @PutMapping("/read-check/all")
    public ResponseEntity<String> markAsReadAll(@RequestBody NotifyDto notifyDto) {
        notifyService.readCheckAll(notifyDto);
        return ResponseEntity.ok("알림을 확인했습니다.");
    }
}
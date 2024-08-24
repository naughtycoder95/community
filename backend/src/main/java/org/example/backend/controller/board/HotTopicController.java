package org.example.backend.controller.board;

import org.example.backend.model.dto.MainPageDto;
import lombok.RequiredArgsConstructor;
import org.example.backend.service.board.HotTopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName : org.example.backend.controller.board
 * fileName : HotTopicController
 * author : GGG
 * date : 2024-06-03
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-03         GGG          최초 생성
 */
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class HotTopicController {
    private final HotTopicService hotTopicService;

    @GetMapping("/hot-topics")
    public ResponseEntity<List<MainPageDto>> getHotTopics() {
        List<MainPageDto> hotTopics = hotTopicService.getHotTopics();
        return ResponseEntity.ok(hotTopics);
    }
}
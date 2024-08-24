package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.MainPageSearchDto;
import org.example.backend.service.board.MainSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : org.example.backend.controller.board
 * fileName : MainSearchController
 * author : GGG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         GGG          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class MainSearchController {
    private final MainSearchService mainSearchService;

    @GetMapping("/main-search")
    public ResponseEntity<Page<MainPageSearchDto>> searchBoards(
            @RequestParam("boardtitle") String boardTitle,
            @PageableDefault(size = 10, sort = "addDate", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<MainPageSearchDto> searchResults = mainSearchService.searchBoard(boardTitle, pageable);
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            log.error("Error searching boards: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.BoardWriteDto;
import org.example.backend.model.entity.board.BoardFile;
import org.example.backend.service.board.BoardEditService;
import org.example.backend.service.board.BoardFileService;
import org.example.backend.service.board.BoardWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName : org.example.backend.controller.board
 * fileName : BoardWriteController
 * author : gumiji
 * date : 5/29/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/29/24         gumiji          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
//@RestControllerAdvice   // 전역적으로 예외처리
public class BoardWriteController {
    private final BoardWriteService boardWriteService;
    private final BoardFileService boardFileService;

    @PostMapping("/board-write")
    public ResponseEntity<Object> create(
            @RequestBody BoardWriteDto boardWriteDto
            ) {
        log.debug("Received BoardWriteDto: {}", boardWriteDto);
        try {
            boardWriteService.save(boardWriteDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 등록에 실패했습니다." + e);
        }
    }
}

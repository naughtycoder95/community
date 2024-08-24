package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.BoardWriteDto;
import org.example.backend.model.dto.board.IClubDto;
import org.example.backend.model.entity.board.BoardFile;
import org.example.backend.model.entity.board.Vote;
import org.example.backend.service.board.BoardEditService;
import org.example.backend.service.board.BoardFileService;
import org.example.backend.service.board.BoardWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName : org.example.backend.controller.board
 * fileName : BoardEditController
 * author : GGG
 * date : 2024-06-11
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-11         GGG          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@RestControllerAdvice   // 전역적으로 예외처리
public class BoardEditController {
    private final BoardEditService boardEditService;

//    수정함수
    @PostMapping("/board-edit")
    public ResponseEntity<Object> edit(
            @RequestBody BoardWriteDto boardWriteDto) {
        log.debug("Received BoardWriteDto: {}", boardWriteDto);
        try {
            boardEditService.update(boardWriteDto);  // 수정된 데이터를 저장하는 서비스 호출
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 수정에 실패했습니다." + e);
        }
    }
}

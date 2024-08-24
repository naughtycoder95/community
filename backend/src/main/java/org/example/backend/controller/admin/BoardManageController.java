package org.example.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.BoardManageDto;
import org.example.backend.model.dto.BoardEditDto;
import org.example.backend.model.entity.CmCode;
import org.example.backend.service.admin.BoardManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName : org.example.backend.controller.board
 * fileName : BoardManageController
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
@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BoardManageController {

    private final BoardManageService boardManageService;

    @GetMapping("/board-mgmt")
    public ResponseEntity<List<BoardManageDto>> getCmCodeList() {
        List<BoardManageDto> cmCodeList = boardManageService.getCmCodeList();
        return ResponseEntity.ok(cmCodeList);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBoard(@RequestBody BoardEditDto boardEditDto) {
        try {
            CmCode newBoard = boardManageService.addBoard(boardEditDto);
            return ResponseEntity.ok(newBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBoard(@RequestBody BoardEditDto boardEditDto) {
        try {
            CmCode updatedBoard = boardManageService.updateBoard(boardEditDto);
            return ResponseEntity.ok(updatedBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{cmCd}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String cmCd) {
        log.info("Deleting board with cmCd: {}", cmCd);
        boardManageService.deleteBoard(cmCd);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reactivate/{cmCd}")
    public ResponseEntity<Void> reactivateBoard(@PathVariable String cmCd) {
        log.info("Reactivating board with cmCd: {}", cmCd);
        boardManageService.reactivateBoard(cmCd);
        return ResponseEntity.noContent().build();
    }
}

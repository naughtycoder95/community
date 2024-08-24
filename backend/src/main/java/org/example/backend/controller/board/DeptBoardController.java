package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.IClubDto;
import org.example.backend.service.board.DeptBoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName : org.example.backend.controller
 * fileName : DeptBoardController
 * author : SAMSUNG
 * date : 2024-05-24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-24         SAMSUNG          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class DeptBoardController {
    private final DeptBoardService deptBoardService;

    // 부서별 게시판 전체조회 - 공지글
    @GetMapping("/dept/notice")
    public ResponseEntity<Object> findAllNotice(
            @RequestParam(defaultValue = "") String smcode
    ) {
        try {
            List<IClubDto> list
                    = deptBoardService.findAllByNotice(smcode);

            if (list.isEmpty() == false) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug("에러 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 부서별 게시판 전체조회 - 일반글
    @GetMapping("/dept")
    public ResponseEntity<Object> findAllByDept(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "") String smcode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<IClubDto> pageList = deptBoardService.findAllByDept(boardTitle, smcode, pageable);
            Map<String, Object> response = new HashMap<>();
            response.put("board", pageList.getContent());            // 게시판배열
            response.put("totalItems", pageList.getTotalElements()); // 전체데이터개수

            if (pageList.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

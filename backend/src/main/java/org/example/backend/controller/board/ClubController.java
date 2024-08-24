package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.IClubDto;
import org.example.backend.model.entity.CmCode;
import org.example.backend.service.board.ClubService;
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
 * packageName : org.example.backend.controller.board
 * fileName : ClubController
 * author : gumiji
 * date : 5/23/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/23/24         gumiji          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    //    동호회 게시판 전체조회
    @GetMapping("/club")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "") String bocode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
//            페이징 객체 생성
            Pageable pageable = PageRequest.of(page, size);

//            전체 조회 서비스 실행
            Page<IClubDto> clubDtoPage
                    = clubService.findByCode(boardTitle, bocode, pageable);
            log.debug("왜11 : " + clubDtoPage);

//            공통 페이징 객체 생성 : 자료구조 맵 사용
            Map<String, Object> response = new HashMap<>();
            response.put("club", clubDtoPage.getContent()); // club 배열
            response.put("currentPage", clubDtoPage.getNumber()); // 현재페이지번호
            response.put("totalItems", clubDtoPage.getTotalElements()); // 총건수(개수)
            response.put("totalPages", clubDtoPage.getTotalPages()); // 총페이지수

            if (clubDtoPage.isEmpty() == false) {
//                조회 성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    동호회 공지 전체조회
    @GetMapping("/club-notice")
    public ResponseEntity<Object> findNotice(
            @RequestParam(defaultValue = "") String bocode
    ) {
        try {
//            전체 조회 서비스 실행
            List<IClubDto> club
                    = clubService.findByCodeAndNotice(bocode);

            if (club.isEmpty() == false) {
//                조회 성공
                return new ResponseEntity<>(club, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug("에러 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    동호회 중분류
    @GetMapping("/club-bocode")
    public ResponseEntity<Object> findBocode() {
        try {
//            전체 조회 서비스 실행
            List<IClubDto> bocode
                    = clubService.findByBocode();
            if (bocode.isEmpty() == false) {
//                조회 성공
                return new ResponseEntity<>(bocode, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug("에러 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    동호회 소분류
    @GetMapping("/club-smcode")
    public ResponseEntity<Object> findSmcode() {
        try {
//            전체 조회 서비스 실행
            List<CmCode> smcode
                    = clubService.findByCmCdNameAndSmcode();
            if (smcode.isEmpty() == false) {
//                조회 성공
                return new ResponseEntity<>(smcode, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug("에러 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.FreeBoardCreateRequest;
import org.example.backend.model.dto.board.IFreeBoardDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.File;
import org.example.backend.service.board.FreeBoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    //    TODO: 최신글 전체 조회 함수 + 검색 + 페이징
    @GetMapping("/free")
    public ResponseEntity<Object> findAll(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);

//            전체 조회 서비스 함수 실행
            Page<IFreeBoardDto> pageList = freeBoardService.findAllByBoardTitleContaining(boardTitle, pageable);

//            vue 로 json 데이터를 전송 : jsp (model == Map(키,값))
            Map<String, Object> response = new HashMap<>();
            response.put("board", pageList.getContent());            // 게시판배열
            response.put("currentPage", pageList.getNumber());       // 현재페이지 번호(x)
            response.put("totalItems", pageList.getTotalElements()); // 전체데이터개수
            response.put("totalPages", pageList.getTotalPages());    // 전체페이지수(x)

            if (pageList.isEmpty() == true) {
//                1) pageList 값이 없으면 : DB 테이블 없음 => NO_CONTENT(203)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
//            TODO: INTERNAL_SERVER_ERROR(500) : 벡엔드 서버 에러
//               아래 코드는 프론트로(웹브라우저) 신호를(500) 보냄
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 인기글 전체 조회 함수 + 검색 + 페이징
    @GetMapping("/free/popular")
    public ResponseEntity<Object> findAllPopular(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);

//            전체 조회 서비스 함수 실행
            Page<IFreeBoardDto> pageList = freeBoardService.findAllByFrBoardTitleContainingAndGoodGreaterThanEqual(boardTitle, pageable);

//            vue 로 json 데이터를 전송 : jsp (model == Map(키,값))
            Map<String, Object> response = new HashMap<>();
            response.put("board", pageList.getContent());            // 게시판배열
            response.put("currentPage", pageList.getNumber());       // 현재페이지 번호(x)
            response.put("totalItems", pageList.getTotalElements()); // 전체데이터개수
            response.put("totalPages", pageList.getTotalPages());    // 전체페이지수(x)

            if (pageList.isEmpty() == true) {
//                1) pageList 값이 없으면 : DB 테이블 없음 => NO_CONTENT(203)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
//            TODO: INTERNAL_SERVER_ERROR(500) : 벡엔드 서버 에러
//               아래 코드는 프론트로(웹브라우저) 신호를(500) 보냄
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    자유게시판 공지 전체조회
    @GetMapping("/free-notice")
    public ResponseEntity<Object> findNotice(
    ) {
        try {
//            전체 조회 서비스 실행
            List<IFreeBoardDto> free
                    = freeBoardService.findByCodeAndNotice();

            if (free.isEmpty() == false) {
//                조회 성공
                return new ResponseEntity<>(free, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug("에러 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 상세조회
    @GetMapping("/free/{boardId}")
    public ResponseEntity<Object> findById(
            @PathVariable long boardId
    ) {
        try {
            Optional<Board> optionalBoard = freeBoardService.findById(boardId);

            if (optionalBoard.isEmpty() == true) {
//                데이터 없음(203)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
//                데이터 있음(200)
                return new ResponseEntity<>(optionalBoard.get()
                        , HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 저장 함수
    @PostMapping("/free-write")
    public ResponseEntity<Object> create(
            @RequestBody FreeBoardCreateRequest request
    ) {
        try {
            freeBoardService.save(request.getBoard(), request.getVoteDtos(), request.getPlace(), request.getFileDtos(), request.getBoardFileDtos());
            return ResponseEntity.status(HttpStatus.CREATED).body(request.getBoard().getBoardTitle() + " 게시글이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
//            500 전송
            log.error("게시글 생성 중 오류 발생:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    TODO: 게시판 파일 조회 함수
    @GetMapping("/file/upload/{fileUuid}")
    ResponseEntity<Object> getImageData(@PathVariable String fileUuid) {
        try {
            int lastDotIdx = fileUuid.lastIndexOf(".");
            String uuid = fileUuid.substring(0, lastDotIdx);
            String extent = fileUuid.substring(lastDotIdx + 1);

            File file = freeBoardService.findFileByUuid(uuid);
            if (file != null) {
                byte[] data = file.getData();
                if (data != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-type", "image/" + extent);
                    return new ResponseEntity<>(data, headers, HttpStatus.OK);
                }
            }
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package org.example.backend.controller.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.board.*;
import org.example.backend.model.dto.board.Reply.ReplyDto;
import org.example.backend.model.entity.board.*;
import org.example.backend.service.board.BoardDetailService;
import org.example.backend.service.board.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * packageName : org.example.backend.controller.board
 * fileName : BoardDetailController
 * author : SAMSUNG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         SAMSUNG          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardDetailController {

    private final BoardDetailService boardDetailService;
    private final ReplyService replyService;

    // 로그인된 회원 정보 조회
    @GetMapping("/board-detail/member")
    public ResponseEntity<Object> findMember(@RequestParam String memberId) {
        try {
            Optional<IUserDto> optional = boardDetailService.findMember(memberId);
            if (optional.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 게시글, 작성자 정보 조회
    @GetMapping("/board-detail")
    public ResponseEntity<Object> findBoardAndMember(@RequestParam Long boardId) {
        try {
            Optional<IBoardDto> optional = boardDetailService.findBoardAndMember(boardId);
            if (optional.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 코드번호로 코드명 조회
    @GetMapping("/board-detail/cmCd")
    public ResponseEntity<Object> findCmCdName(@RequestParam String cmCd) {
        try {
            Optional<IBoardDetailDto> optional = boardDetailService.findCmCdName(cmCd);
            if (optional.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 글번호로 투표 조회
    @GetMapping("/board-detail/vote")
    public ResponseEntity<Object> getAll(@RequestParam Long boardId){
        try {
            List<Vote> list = boardDetailService.findVote(boardId);
            if (list.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 투표 회원 조회
    @GetMapping("/board-detail/vote-member")
    public ResponseEntity<Object> findVoteMember(@RequestParam Long boardId, @RequestParam String memberId) {
        try {
            Optional<VoteMember> optional = boardDetailService.findVoteMember(boardId, memberId);
            if (optional.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 글번호로 장소 조회 (게시글 하나당 장소 하나)
    @GetMapping("/board-detail/place")
    public ResponseEntity<Object> findPlace(@RequestParam Long boardId) {
        try {
            log.debug("Controller 문제?? boardId :: ", boardId);
            Optional<Place> optional = boardDetailService.findPlace(boardId);
            if (optional.isEmpty()) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 글번호로 이미지 조회
    @GetMapping("/board-detail/board-img")
    public ResponseEntity<Object> findBoardImg(@RequestParam Long boardId) {
        try {
            List<IBoardDetailDto> list = boardDetailService.findBoardImg(boardId);
            if (list.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 추천 데이터 존재하는지 확인
    @GetMapping("/board-detail/recommend-exist")
    public ResponseEntity<Object> existsRecommend(@RequestParam Long boardId, @RequestParam String memberId) {
        try {
            Integer recommend = boardDetailService.existsRecommend(boardId, memberId);
            return new ResponseEntity<>(recommend, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 추천 저장함수
    @PostMapping("/board-detail/recommend-exist")
    public ResponseEntity<Object> createRecommend(
            @RequestParam Long boardId,
            @RequestParam String memberId,
            @RequestParam String currentUrl
    ) {
        try {
            Recommend recommend = new Recommend(boardId, memberId);
            boardDetailService.saveRecommend(recommend, currentUrl);
            return new ResponseEntity<>("추천 저장 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 추천 수 카운트
    @GetMapping("/board-detail/recommend-count")
    public ResponseEntity<Object> countRecommend(@RequestParam Long boardId) {
        try {
            Integer count = boardDetailService.countRecommend(boardId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 첨부파일 다운로드
    @GetMapping("/file/upload2/{uuid}")
    public ResponseEntity<byte[]> fileDownload(@PathVariable String uuid) {
        Optional<File> file = boardDetailService.fileDownload(uuid);

        String fileName = file.get().getFileName();
        String encodeName = null;
        try {
            encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.debug("인코딩 오류", e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodeName )
                .body(file.get().getData()); // BLOB 데이터
    }

    // 글번호로 댓글 조회
    @GetMapping("/board-detail/reply")
    public ResponseEntity<Object> findReplyCount(
            @RequestParam Long boardId,
            @PageableDefault Pageable pageable) {
        try {
            Page<IReplyDto> list = replyService.findReply(boardId, pageable);
            if (list.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 대댓글 조회
    @GetMapping("/board-detail/re-reply")
    public ResponseEntity<Object> findReReplyCount(@RequestParam Long boardId, @RequestParam Long replyId) {
        try {
            List<IReplyDto> list = replyService.findReReply(boardId, replyId);
            if (list.isEmpty() == true) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 수 조회
    @GetMapping("/board-detail/reply/count")
    public ResponseEntity<Object> countReply(@RequestParam Long boardId) {
        try {
            Integer count = replyService.countReply(boardId);
            if (count == null) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(count, HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 및 대댓글 저장
    @PostMapping("/board-detail/reply")
    public ResponseEntity<Object> createReply(
            @RequestParam Long boardId,
            @RequestParam String memberId,
            @RequestParam String reply,
            @RequestParam(required = false) Long reReply,
            @RequestParam String currentUrl,
            @RequestPart(value = "file", required = false) MultipartFile file
            ) {
        try {
            ReplyDto replyDto = new ReplyDto(null, boardId, memberId, reply, reReply);
            replyService.saveReply(replyDto, file, currentUrl);
            return new ResponseEntity<>("댓글 저장 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 및 대댓글 수정
    @PutMapping("/board-detail/reply")
    public ResponseEntity<Object> updateReply(
            @RequestParam Long replyId,
            @RequestParam Long boardId,
            @RequestParam String memberId,
            @RequestParam String reply,
            @RequestParam(required = false) Long reReply,
            @RequestParam boolean isFileDeleted,
            @RequestPart(value = "file", required = false) MultipartFile file
            ) {
        try {
            ReplyDto replyDto = new ReplyDto(replyId, boardId, memberId, reply, reReply);
            replyService.updateReply(replyDto, file, isFileDeleted);
            return new ResponseEntity<>("댓글 수정 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 및 대댓글 삭제
    @DeleteMapping("/board-detail/reply/delete")
    public ResponseEntity<Object> deleteReply(@RequestParam Long replyId) {
        try {
            // DB 삭제 서비스 실행
            log.debug("컨트롤러 실행 ::: ", replyId);
            replyService.removeReply(replyId);
            return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 글 신고 데이터 저장
    @PostMapping("/board-detail/report")
    public ResponseEntity<Object> createReport(@RequestBody Report report) {
        try {
            boardDetailService.saveReport(report);
            return new ResponseEntity<>("신고 저장 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 글 신고 데이터 존재하는지 확인
    @GetMapping("/board-detail/report-exist")
    public ResponseEntity<Object> existsReport(@RequestParam Long boardId, @RequestParam String memberId) {
        try {
            Integer report = boardDetailService.existsReport(boardId, memberId);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 신고 데이터 존재하는지 확인
    @GetMapping("/board-detail/reply-report-exist")
    public ResponseEntity<Object> existsReplyReport(@RequestParam Long replyId, @RequestParam String memberId) {
        try {
            Integer replyReport= boardDetailService.existsReplyReport(replyId, memberId);
            return new ResponseEntity<>(replyReport, HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 이미지 저장
    @PostMapping("/board-detail/file/upload")
    public ResponseEntity<Object> create(
            @RequestParam MultipartFile image   // 첨부파일은 MultipartFile 형태로 받아야함
    ) {
        try {
            // DB 서비스 함수 실행
            replyService.saveReplyFile(null, image);
            return new ResponseEntity<>("업로드 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 댓글 신고 데이터 저장
    @PostMapping("/board-detail/reply-report")
    public ResponseEntity<Object> createReplyReport(@RequestBody ReplyReport replyReport) {
        try {
            replyService.saveReplyReport(replyReport);
            return new ResponseEntity<>("댓글 신고 저장 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    // 투표 저장
    @PostMapping("/board-detail/vote")
    public ResponseEntity<Object> createVoteMember(@RequestBody VoteMember voteMember) {
        try {
            boardDetailService.saveVoteAndCount(voteMember);
            return new ResponseEntity<>("투표 저장 성공", HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }


    // 예외 처리 메서드
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            log.error("잘못된 인자가 전달되었습니다", e);
            return new ResponseEntity<>("잘못된 인자가 전달되었습니다", HttpStatus.BAD_REQUEST);
        } else if (e instanceof EntityNotFoundException) {
            log.error("해당 엔티티를 찾을 수 없습니다", e);
            return new ResponseEntity<>("해당 엔티티를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        } else {
            log.error("서버 오류가 발생했습니다", e);
            return new ResponseEntity<>("서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글, 작성자 정보 상세 조회
    @GetMapping("/board-detail/edit")
    public ResponseEntity<Object> findByBoardAndMember(@RequestParam Long boardId) {
        log.debug("Received boardId: {}", boardId);
        try {
            Optional<IBoardDto> optional = boardDetailService.findBoardAndMember(boardId);
            if (!optional.isPresent()) {
                return new ResponseEntity<>("데이터 없음", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/board-detail/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId) {
        try {
            log.debug(":" + boardId);
            boardDetailService.deleteBoard(boardId);
            return ResponseEntity.ok("게시글 삭제가 완료되었습니다.");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/board-detail/update/{boardId}")
    public ResponseEntity<?> updateBoard(@RequestParam Long boardId, @RequestBody IBoardDto boardDto) {
        try {
            boardDetailService.updateBoard(boardId, boardDto);
            return ResponseEntity.ok("게시글 수정이 완료되었습니다.");
        } catch (Exception e) {
            return handleException(e);
        }
    }
}

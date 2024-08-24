package org.example.backend.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.service.board.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : org.example.backend.controller.board
 * fileName : VoteController
 * author : kimtaewan
 * date : 2024-05-28
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-28         kimtaewan          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PutMapping("/{boardId}/{voteId}/{memberId}")
    public ResponseEntity updateVote(@PathVariable Long voteId, @PathVariable String memberId, @PathVariable Long boardId) {
        try {
            voteService.updateVoteCnt(voteId, memberId, boardId);
            return ResponseEntity.ok("투표가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("투표가 정상적으로 완료되지 않았습니다.");
        }
    }
}

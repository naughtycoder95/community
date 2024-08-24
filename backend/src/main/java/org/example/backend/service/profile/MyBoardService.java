package org.example.backend.service.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.IMyReplyDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.repository.profile.MyBoardRepository;
import org.example.backend.repository.profile.MyCommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * packageName : org.example.backend.service.profile
 * fileName : MyBoardService
 * author : sjuk2
 * date : 2024-05-30
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-30         sjuk2          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyBoardService {

    private final MyBoardRepository myBoardRepository;

    private final MyCommentRepository myCommentRepository;

    public Page<Board> findByMemberIdOfBoard(
            String memberId, Pageable pageable
    ) {
        Page<Board> page = myBoardRepository.findByMemberIdOfBoard(memberId, pageable);
        return page;
    }

    public Page<IMyReplyDto> findByMemberIdOfComment(
            String memberId, Pageable pageable
    ) {
        Page<IMyReplyDto> page = myCommentRepository.findByMemberIdOfComment(memberId, pageable);
        return page;
    }
}

package org.example.backend.repository.board;

import org.example.backend.model.entity.board.ReplyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : ReplyFileRepository
 * author : SAMSUNG
 * date : 2024-06-09
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-09         SAMSUNG          최초 생성
 */
@Repository
public interface ReplyFileRepository extends JpaRepository<ReplyFile, Long> {
    void deleteByUuid(String uuid);
    List<ReplyFile> findByReplyId(Long replyId);

}

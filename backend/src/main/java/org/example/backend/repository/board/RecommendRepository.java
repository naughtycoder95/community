package org.example.backend.repository.board;

import org.example.backend.model.common.BoardIdMemberIdPk;
import org.example.backend.model.entity.board.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.backend.repository.board
 * fileName : RecommendRepository
 * author : SAMSUNG
 * date : 2024-06-06
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-06         SAMSUNG          최초 생성
 */
@Repository
public interface RecommendRepository extends JpaRepository<Recommend, BoardIdMemberIdPk> {
    void deleteByBoardId(Long boardId);
}

package org.example.backend.repository.board;

import org.example.backend.model.entity.board.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : VoteRepository
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
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query(value = "UPDATE TB_VOTE\n" +
            "SET vote_cnt = vote_cnt + 1\n" +
            "WHERE voteId = :voteId\n" +
            "AND NOT EXISTS (\n" +
            "SELECT 1\n" +
            "FROM TB_VOTE_MEMBER\n" +
            "WHERE boardId = :boardId\n" +
            "AND memberId = :memberId\n" +
            ")", nativeQuery = true)
    void updateVoteCnt(@Param("voteId") Long voteId, @Param("memberId") String memberId, @Param("boardId") Long boardId);


    @Query(value = "UPDATE TB_VOTE " +
            "SET STATUS = 'N' " +
            "WHERE DEL_DATE = :delDate ", nativeQuery = true)
    List<Vote> updateStatus(@Param("delDate") String delDate);

    void deleteByBoardId(Long boardId);

    List<Vote> findByBoardId(Long boardId);
}

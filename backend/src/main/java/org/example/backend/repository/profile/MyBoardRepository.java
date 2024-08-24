package org.example.backend.repository.profile;

import org.example.backend.model.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.backend.repository.profile
 * fileName : MyBoardRepository
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
@Repository
public interface MyBoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT *\n" +
            "FROM TB_BOARD\n" +
            "WHERE MEMBER_ID = :memberId\n" +
            "AND STATUS = 'Y'\n" +
            "ORDER BY ADD_DATE DESC"
    , nativeQuery = true)
    Page<Board> findByMemberIdOfBoard(@Param("memberId") String memberId, Pageable pageable);
}

package org.example.backend.repository.board;

import org.example.backend.model.dto.MainPageSearchDto;
import org.example.backend.model.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.backend.repository.board
 * fileName : MainSearchRepository
 * author : GGG
 * date : 2024-05-28
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-28         GGG          최초 생성
 */
@Repository
public interface MainSearchRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT " +
            "B.BOARD_ID AS boardId, " +
            "B.BOARD_TITLE AS boardTitle, " +
            "B.ADD_DATE AS addDate, " +
            "B.BOCODE AS boCode, " +
            "B.SMCODE AS smCode, " +
            "M.NICKNAME AS nickName, " +
            "C.CM_CD AS cmCode, " +
            "C.CM_CD_NAME AS cmCodeName, " +
            "(SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) AS good, " +
            "NVL(R2.total_reply_count, 0) AS totalReplyCount " +
            "FROM TB_BOARD B " +
            "JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
            "JOIN TB_CM_CODE C ON B.BOCODE = C.CM_CD " +
            "LEFT JOIN (SELECT BOARD_ID, COUNT(*) AS total_reply_count " +
            "           FROM TB_REPLY " +
            "           WHERE STATUS = 'Y' " +
            "           GROUP BY BOARD_ID) R2 ON B.BOARD_ID = R2.BOARD_ID " +
            "WHERE B.BOCODE <> 'BO01' " +
            "AND B.STATUS = 'Y' " +
            "AND B.NOTICE_YN = 'N' " +
            "AND LOWER(B.BOARD_TITLE) LIKE LOWER('%' || :boardtitle || '%')" +
            "ORDER BY B.ADD_DATE DESC",
            countQuery = "SELECT count(*) " +
                    "FROM TB_BOARD B " +
                    "JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
                    "JOIN TB_CM_CODE C ON B.BOCODE = C.CM_CD " +
                    "WHERE B.BOCODE <> 'BO01' " +
                    "AND B.STATUS = 'Y' " +
                    "AND B.NOTICE_YN = 'N' " +
                    "AND LOWER(B.BOARD_TITLE) LIKE LOWER('%' || :boardtitle || '%')",
            nativeQuery = true)
    Page<MainPageSearchDto> searchAllByBoard(@Param("boardtitle") String boardtitle, Pageable pageable);
}

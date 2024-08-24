package org.example.backend.repository.board;

import org.example.backend.model.dto.board.IFreeBoardDto;
import org.example.backend.model.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreeBoardRepository extends JpaRepository<Board, Long> {
    // 최신글 전체조회
    @Query(value = "SELECT B.BOARD_ID AS boardId, " +
            "B.BOARD_TITLE AS boardTitle, " +
            "M.NICKNAME AS nickname, " +
            "B.ADD_DATE AS addDate, " +
            "(SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) AS good, " +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM TB_BOARD B " +
            "LEFT JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
            "WHERE B.BOCODE = 'BO03' " +
            "AND B.STATUS = 'Y' " +
            "AND B.NOTICE_YN = 'N' " +
            "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%' " +
            "ORDER BY B.ADD_DATE DESC",
            countQuery = "SELECT count(*) " +
                    "FROM TB_BOARD B " +
                    "LEFT JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
                    "WHERE B.BOCODE = 'BO03' " +
                    "AND B.STATUS = 'Y' " +
                    "AND B.NOTICE_YN = 'N' " +
                    "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%'",
            nativeQuery = true)
    Page<IFreeBoardDto> findAllByFrBoardTitleContaining(@Param("boardTitle") String boardTitle,
                                                        Pageable pageable
    );

    //    추천수 10이상 + 최신순 전체조회
    @Query(value = "SELECT B.BOARD_ID AS boardId, " +
            "B.BOARD_TITLE AS boardTitle, " +
            "M.NICKNAME AS nickname, " +
            "B.ADD_DATE AS addDate, " +
            "(SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) AS good, " +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM TB_BOARD B " +
            "LEFT JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
            "WHERE B.BOCODE = 'BO03' " +
            "AND B.STATUS = 'Y' " +
            "AND B.NOTICE_YN = 'N' " +
            "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%' " +
            "AND (SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) >= 10 " +
            "ORDER BY B.ADD_DATE DESC",
            countQuery = "SELECT count(*) " +
                    "FROM TB_BOARD B " +
                    "LEFT JOIN TB_MEMBER M ON B.MEMBER_ID = M.MEMBER_ID " +
                    "WHERE B.BOCODE = 'BO03' " +
                    "AND B.STATUS = 'Y' " +
                    "AND B.NOTICE_YN = 'N' " +
                    "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%' " +
                    "AND (SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) >= 10",
            nativeQuery = true)
    Page<IFreeBoardDto> findAllByFrBoardTitleContainingAndGoodGreaterThanEqual(@Param("boardTitle") String boardTitle,
                                                                               Pageable pageable
    );

    //    자유게시판 공지 전체조회
    @Query(value = "SELECT B.BOARD_ID AS boardId,\n" +
            "B.BOARD_TITLE AS boardTitle,\n" +
            "M.NICKNAME AS nickname,\n" +
            "B.ADD_DATE AS addDate,\n" +
            "(SELECT COUNT(*) FROM TB_RECOMMEND R WHERE R.BOARD_ID = B.BOARD_ID) AS good, " +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM TB_BOARD B, TB_MEMBER M\n" +
            "WHERE B.BOCODE = 'BO03'\n" +
            "AND B.MEMBER_ID = M.MEMBER_ID\n" +
            "AND B.STATUS = 'Y'\n" +
            "AND B.NOTICE_YN = 'Y'\n" +
            "ORDER BY B.ADD_DATE DESC",
            nativeQuery = true)
    List<IFreeBoardDto> findByFreeNotice();
}

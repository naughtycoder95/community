package org.example.backend.repository.board;

import org.example.backend.model.dto.board.IClubDto;
import org.example.backend.model.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : DeptBoardRepository
 * author : SAMSUNG
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         SAMSUNG          최초 생성
 */
@Repository
public interface DeptBoardRepository extends JpaRepository<Board, Long> {
    // 부서별 게시판 전체조회 - 공지글
    @Query(value = "SELECT \n" +
            "B.BOARD_ID AS boardId,\n" +
            "B.BOARD_TITLE AS boardTitle, \n" +
            "M.MEMBER_NAME AS memberName,\n" +
            "B.ADD_DATE AS addDate,\n" +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM TB_BOARD B, TB_MEMBER M\n" +
            "WHERE B.MEMBER_ID = M.MEMBER_ID\n" +
            "AND B.BOCODE = 'BO01'\n" +
            "AND B.STATUS = 'Y'\n" +
            "AND B.NOTICE_YN = 'Y'\n" +
            "AND B.SMCODE = :smcode\n" +
            "ORDER BY B.ADD_DATE DESC", nativeQuery = true)
    List<IClubDto> findAllByNotice(@Param("smcode") String smcode);

    // 부서별 게시판 전체조회 - 일반글
    @Query(value = "SELECT \n" +
            "B.BOARD_ID AS boardId,\n" +
            "B.BOARD_TITLE AS boardTitle,\n" +
            "M.MEMBER_NAME AS memberName,\n" +
            "B.ADD_DATE AS addDate,\n" +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM TB_BOARD B, TB_MEMBER M\n" +
            "WHERE B.MEMBER_ID = M.MEMBER_ID\n" +
            "AND B.BOCODE = 'BO01'\n" +
            "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%'\n" +
            "AND B.STATUS = 'Y'\n" +
            "AND B.NOTICE_YN = 'N'\n" +
            "AND B.SMCODE = :smcode\n" +
            "ORDER BY B.ADD_DATE DESC"
            , countQuery = "SELECT count(*)" +
                            "FROM TB_BOARD B, TB_MEMBER M\n" +
                            "WHERE B.MEMBER_ID = M.MEMBER_ID\n" +
                            "AND B.BOCODE = 'BO01'\n" +
                            "AND B.BOARD_TITLE LIKE '%' || :boardTitle || '%'\n" +
                            "AND B.STATUS = 'Y'\n" +
                            "AND B.NOTICE_YN = 'N'\n" +
                            "AND B.SMCODE = :smcode\n" +
                            "ORDER BY B.ADD_DATE DESC"
            , nativeQuery = true)
    Page<IClubDto> findAllByDept(@Param("boardTitle") String boardTitle, @Param("smcode") String smcode, Pageable pageable);
}

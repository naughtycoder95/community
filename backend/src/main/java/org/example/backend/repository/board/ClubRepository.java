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
 * fileName : ClubRepository
 * author : gumiji
 * date : 5/22/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/22/24         gumiji          최초 생성
 */
@Repository
public interface ClubRepository extends JpaRepository<Board, Long> {
    //    동호회 게시판 전체조회
    @Query(value = "SELECT \n" +
            "    B.BOARD_ID AS boardId,\n" +
            "    M.MEMBER_NAME AS memberName,\n" +
            "    B.BOARD_TITLE AS boardTitle,\n" +
            "    B.ADD_DATE AS addDate,\n" +
            "    C.CM_CD_NAME AS cmCdName,\n" +
            "    B.BOCODE AS bocode,\n" +
            "    B.SMCODE AS smcode,\n" +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM \n" +
            "    TB_BOARD B,\n" +
            "    TB_MEMBER M,\n" +
            "    TB_CM_CODE C\n" +
            "WHERE \n" +
            "    B.BOCODE = :bocode\n" +
            "    AND B.MEMBER_ID = M.MEMBER_ID\n" +
            "    AND B.SMCODE = C.CM_CD\n" +
            "    AND B.BOARD_TITLE LIKE '%' ||:boardTitle|| '%'\n" +
            "    AND B.STATUS = 'Y'\n" +
            "    AND B.NOTICE_YN = 'N'\n" +
            "ORDER BY \n" +
            "    B.ADD_DATE DESC",
            countQuery = "SELECT count(*)\n" +
                    "FROM TB_BOARD B, TB_MEMBER M, TB_CM_CODE C\n" +
                    "WHERE B.BOCODE = :bocode\n" +
                    "AND B.MEMBER_ID = M.MEMBER_ID\n" +
                    "AND B.SMCODE = C.CM_CD\n" +
                    "AND B.BOARD_TITLE LIKE '%' ||:boardTitle|| '%'\n" +
                    "AND B.STATUS = 'Y'\n" +
                    "AND B.NOTICE_YN = 'N'\n" +
                    "ORDER BY B.ADD_DATE DESC",
            nativeQuery = true)
    Page<IClubDto> findByCode(@Param("boardTitle") String boardTitle,
                              @Param("bocode") String bocode,
                              Pageable pageable);

    //    동호회 공지 전체조회
    @Query(value = "SELECT \n" +
            "    B.BOARD_ID AS boardId,\n" +
            "    M.MEMBER_NAME AS memberName,\n" +
            "    B.BOARD_TITLE AS boardTitle,\n" +
            "    B.ADD_DATE AS addDate,\n" +
            "    C.CM_CD_NAME AS cmCdName,\n" +
            "    B.BOCODE AS bocode,\n" +
            "    B.SMCODE AS smcode,\n" +
            "    (SELECT count(*)\n" +
            "FROM TB_REPLY R\n" +
            "WHERE R.BOARD_ID = B.BOARD_ID\n" +
            "AND STATUS = 'Y') AS replyCount\n" +
            "FROM \n" +
            "    TB_BOARD B,\n" +
            "    TB_MEMBER M,\n" +
            "    TB_CM_CODE C\n" +
            "WHERE \n" +
            "    B.BOCODE = :bocode\n" +
            "    AND B.MEMBER_ID = M.MEMBER_ID\n" +
            "    AND B.SMCODE = C.CM_CD\n" +
            "    AND B.BOARD_TITLE LIKE '%' || '' || '%'\n" +
            "    AND B.STATUS = 'Y'\n" +
            "    AND B.NOTICE_YN = 'Y'\n" +
            "ORDER BY \n" +
            "    B.ADD_DATE DESC",
            nativeQuery = true)
    List<IClubDto> findByCodeAndNotice(@Param("bocode") String bocode);

    //    동호회 중메뉴
    @Query(value = "SELECT CM_CD_NAME AS cmCdName,\n" +
            "CM_CD AS cmCd " +
            "FROM TB_CM_CODE\n" +
            "WHERE UP_CM_CD LIKE 'BO02'\n" +
            "AND STATUS = 'Y'\n" +
            "ORDER BY CM_CD",
            nativeQuery = true)
    List<IClubDto> findByBocode();

}

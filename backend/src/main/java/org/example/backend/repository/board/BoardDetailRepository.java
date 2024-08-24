package org.example.backend.repository.board;

import org.example.backend.model.dto.board.DelBoardDto;
import org.example.backend.model.dto.board.IBoardDetailDto;
import org.example.backend.model.dto.board.IBoardDto;
import org.example.backend.model.dto.board.IUserDto;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.Place;
import org.example.backend.model.entity.board.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName : org.example.backend.repository.board
 * fileName : BoardDetailRepository
 * author : SAMSUNG
 * date : 2024-05-31
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-31         SAMSUNG          최초 생성
 */
@Repository
public interface BoardDetailRepository extends JpaRepository<Board, Long> {
    // 로그인된 회원 정보 조회
    @Query(value = "SELECT MEMBER_ID AS memberId,\n" +
            "MEMBER_NAME AS memberName,\n" +
            "NICKNAME AS nickname,\n" +
            "MEMBER_CODE AS memberCode,\n" +
            "DEPT_CODE AS deptCode,\n" +
            "POS_CODE AS posCode\n" +
            "FROM TB_MEMBER\n" +
            "WHERE MEMBER_ID = :memberId"
            , nativeQuery = true)
    Optional<IUserDto> findMember(@Param("memberId") String memberId);

    // 게시글, 작성자 정보 조회
    @Query(value = "SELECT M.MEMBER_ID AS memberId," +
            "M.MEMBER_NAME AS memberName,\n" +
            "M.NICKNAME AS nickname,\n" +
            "B.BOARD_ID AS boardId,\n" +
            "B.BOARD_TITLE AS boardTitle,\n" +
            "B.BOARD_CONTENT AS boardContent,\n" +
            "B.NOTICE_YN AS noticeYn, " +
            "B.BOCODE AS bocode,\n" +
            "B.SMCODE AS smcode,\n" +
            "B.ADD_DATE AS addDate\n" +
            "FROM TB_MEMBER M, TB_BOARD B\n" +
            "WHERE M.MEMBER_ID = B.MEMBER_ID\n" +
            "AND B.BOARD_ID = :boardId\n" +
            "AND B.STATUS = 'Y'"
            , nativeQuery = true)
    Optional<IBoardDto> findBoardAndMember(@Param("boardId") Long boardId);

    // 코드번호로 코드명 조회
    @Query(value = "SELECT c.CM_CD AS cmCd, " +
            "     c.CM_CD_NAME AS cmCdName " +
            "FROM TB_CM_CODE c WHERE c.CM_CD = :cmCd",
            nativeQuery = true)
    Optional<IBoardDetailDto> findCmCdName(@Param("cmCd") String cmCd);

    // 글번호로 장소 조회 (게시글 하나당 장소 하나)
    @Query(value = "SELECT p FROM Place p WHERE p.boardId = :boardId")
    Optional<Place> findPlace(@Param("boardId") Long boardId);

    // 글번호로 이미지 조회
    @Query(value = "SELECT BF.BOARD_ID AS boardId,\n" +
            "              F.UUID AS uuid,\n" +
            "              F.FILE_URL AS fileUrl,\n" +
            "              F.FILE_NAME AS fileName " +
            "        FROM TB_BOARD_FILE BF, TB_FILE F\n" +
            "        WHERE BF.UUID = F.UUID\n" +
            "        AND F.STATUS = 'Y'\n" +
            "        AND BF.BOARD_ID = :boardId"
            , nativeQuery = true)
    List<IBoardDetailDto> findBoardImg(@Param("boardId") Long boardId);

    // 추천 데이터 존재하는지 확인
    @Query(value = "SELECT count(*)\n" +
            "FROM TB_RECOMMEND \n" +
            "WHERE BOARD_ID = :boardId\n" +
            "AND MEMBER_ID = :memberId"
            , nativeQuery = true)
    Integer existsRecommend(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    // 신고 데이터 존재하는지 확인
    @Query(value = "SELECT count(*)\n" +
            "FROM TB_REPORT \n" +
            "WHERE BOARD_ID = :boardId\n" +
            "AND MEMBER_ID = :memberId"
            , nativeQuery = true)
    Integer existsReport(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    // 댓글 신고 데이터 존재하는지 확인
    @Query(value = "SELECT count(*)\n " +
            "FROM TB_REPLY_REPORT \n " +
            "WHERE REPLY_ID = :replyId\n " +
            "AND MEMBER_ID = :memberId "
            , nativeQuery = true)
    Integer existsReplyReport(@Param("replyId") Long boardId, @Param("memberId") String memberId);

    // 추천 수 카운트
    @Query(value = "SELECT count(*)\n" +
            "FROM TB_RECOMMEND \n" +
            "WHERE BOARD_ID = :boardId"
            , nativeQuery = true)
    Integer countRecommend(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT B.BOARD_ID AS boardId, F.UUID AS uuid, R.REPLY_ID AS replyId\n" +
            "FROM TB_BOARD B\n" +
            "LEFT JOIN TB_BOARD_FILE BF ON B.BOARD_ID = BF.BOARD_ID\n" +
            "LEFT JOIN TB_REPLY R ON B.BOARD_ID = R.BOARD_ID\n" +
            "RIGHT JOIN TB_FILE F ON BF.UUID = F.UUID\n" +
            "LEFT JOIN TB_PLACE P ON B.BOARD_ID = P.BOARD_ID\n" +
            "LEFT JOIN TB_VOTE V ON B.BOARD_ID = V.BOARD_ID\n" +
            "LEFT JOIN TB_VOTE_MEMBER VM ON B.BOARD_ID = VM.BOARD_ID\n" +
            "LEFT JOIN TB_RECOMMEND RE ON B.BOARD_ID = RE.BOARD_ID\n" +
            "WHERE B.BOARD_ID = :boardId"
            , nativeQuery = true)
    List<DelBoardDto> findByBoardId(@Param("boardId") Long boardId);
}

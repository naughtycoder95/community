package org.example.backend.repository.board;

import org.example.backend.model.dto.board.IReplyDto;
import org.example.backend.model.dto.board.Reply.IDelReplyDto;
import org.example.backend.model.entity.board.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.backend.repository.board
 * fileName : ReplyRepository
 * author : kimtaewan
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         kimtaewan          최초 생성
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 글번호로 댓글 조회
    @Query(value = "SELECT B.REPLY_ID AS replyId,\n" +
            "        A.FILE_URL AS fileUrl,\n" +
            "        A.FILE_NAME AS fileName,\n" +
            "        B.BOARD_ID AS boardId,\n" +
            "        B.MEMBER_ID AS memberId,\n" +
            "        B.MEMBER_NAME AS memberName,\n" +
            "        B.NICKNAME AS nickname,\n" +
            "        B.RE_REPLY AS reReply,\n" +
            "        B.REPLY AS reply,\n" +
            "        B.ADD_DATE AS addDate\n" +
            "FROM (SELECT RF.REPLY_ID,\n" +
            "        F.FILE_URL,\n" +
            "        F.FILE_NAME\n" +
            "FROM TB_FILE F, TB_REPLY_FILE RF\n" +
            "WHERE F.UUID = RF.UUID\n" +
            "AND F.STATUS = 'Y'\n" +
            "AND RF.STATUS = 'Y') A, \n" +
            "(SELECT R.BOARD_ID,\n" +
            "        R.REPLY_ID,\n" +
            "        M.MEMBER_ID,\n" +
            "        M.MEMBER_NAME,\n" +
            "        M.NICKNAME,\n" +
            "        R.RE_REPLY,\n" +
            "        R.REPLY,\n" +
            "        R.ADD_DATE\n" +
            "FROM TB_REPLY R, TB_MEMBER M\n" +
            "WHERE R.MEMBER_ID = M.MEMBER_ID\n" +
            "AND R.BOARD_ID = :boardId\n" +
            "AND R.RE_REPLY IS NULL\n" +
            "AND R.STATUS = 'Y') B\n" +
            "WHERE A.REPLY_ID(+) = B.REPLY_ID " +
            "ORDER BY B.ADD_DATE ASC"
            , countQuery = "SELECT count(*) " +
                            "FROM (SELECT RF.REPLY_ID,\n" +
                            "        F.FILE_URL,\n" +
                            "        F.FILE_NAME\n" +
                            "FROM TB_FILE F, TB_REPLY_FILE RF\n" +
                            "WHERE F.UUID = RF.UUID\n" +
                            "AND F.STATUS = 'Y'\n" +
                            "AND RF.STATUS = 'Y') A, \n" +
                            "(SELECT R.BOARD_ID,\n" +
                            "        R.REPLY_ID,\n" +
                            "        M.MEMBER_ID,\n" +
                            "        M.MEMBER_NAME,\n" +
                            "        M.NICKNAME,\n" +
                            "        R.RE_REPLY,\n" +
                            "        R.REPLY,\n" +
                            "        R.ADD_DATE\n" +
                            "FROM TB_REPLY R, TB_MEMBER M\n" +
                            "WHERE R.MEMBER_ID = M.MEMBER_ID\n" +
                            "AND R.BOARD_ID = :boardId\n" +
                            "AND R.RE_REPLY IS NULL\n" +
                            "AND R.STATUS = 'Y') B\n" +
                            "WHERE A.REPLY_ID(+) = B.REPLY_ID"
            , nativeQuery = true)
    Page<IReplyDto> findReply(@Param("boardId") Long boardId, Pageable pageable);

    // 대댓글 조회
    @Query(value = "SELECT B.REPLY_ID AS replyId,\n" +
            "        A.FILE_URL AS fileUrl,\n" +
            "        A.FILE_NAME AS fileName,\n" +
            "        B.BOARD_ID AS boardId,\n" +
            "        B.MEMBER_ID AS memberId,\n" +
            "        B.MEMBER_NAME AS memberName,\n" +
            "        B.NICKNAME AS nickname,\n" +
            "        B.RE_REPLY AS reReply,\n" +
            "        B.REPLY AS reply,\n" +
            "        B.ADD_DATE AS addDate\n" +
            "FROM (SELECT RF.REPLY_ID,\n" +
            "        F.FILE_URL,\n" +
            "        F.FILE_NAME\n" +
            "FROM TB_FILE F, TB_REPLY_FILE RF\n" +
            "WHERE F.UUID = RF.UUID\n" +
            "AND F.STATUS = 'Y'\n" +
            "AND RF.STATUS = 'Y') A, \n" +
            "(SELECT R.BOARD_ID,\n" +
            "        R.REPLY_ID,\n" +
            "        M.MEMBER_ID,\n" +
            "        M.MEMBER_NAME,\n" +
            "        M.NICKNAME,\n" +
            "        R.RE_REPLY,\n" +
            "        R.REPLY,\n" +
            "        R.ADD_DATE\n" +
            "FROM TB_REPLY R, TB_MEMBER M\n" +
            "WHERE R.MEMBER_ID = M.MEMBER_ID\n" +
            "AND R.BOARD_ID = :boardId\n" +
            "AND R.RE_REPLY = :replyId\n" +
            "AND R.STATUS = 'Y') B\n" +
            "WHERE A.REPLY_ID(+) = B.REPLY_ID " +
            "ORDER BY B.ADD_DATE ASC"
            , nativeQuery = true)
    List<IReplyDto> findReReply(@Param("boardId") Long boardId, @Param("replyId") Long replyId);

    // 댓글 수 조회
    @Query(value = "SELECT count(*)" +
            "FROM TB_REPLY\n" +
            "WHERE BOARD_ID = :boardId\n" +
            "AND STATUS = 'Y'"
            , nativeQuery = true)
    Integer countReply(@Param("boardId") Long boardId);

    @Query(value = "SELECT DISTINCT R.REPLY_ID AS replyId, F.UUID AS uuid\n" +
            "FROM TB_REPLY R\n" +
            "LEFT JOIN TB_REPLY_FILE RF ON  R.REPLY_ID = RF.REPLY_ID\n" +
            "LEFT JOIN TB_FILE F ON RF.UUID = F.UUID\n" +
            "WHERE R.RE_REPLY = :replyId\n" +
            "OR R.REPLY_ID = :replyId", nativeQuery = true)
    List<IDelReplyDto> findByReplyId (@Param("replyId") Long replyId);

    boolean existsByMemberId(String memberId);

    List<Reply> findByMemberId(String memberId);
}

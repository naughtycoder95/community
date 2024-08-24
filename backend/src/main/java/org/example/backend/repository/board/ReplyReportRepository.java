package org.example.backend.repository.board;

import org.example.backend.model.dto.IReplyReportDto;
import org.example.backend.model.entity.board.ReplyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : org.example.backend.repository.board
 * fileName : ReplyReportRepository
 * author : SAMSUNG
 * date : 2024-06-07
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-07         SAMSUNG          최초 생성
 */
@Repository
public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {
    @Query(value = "SELECT B.BOARD_ID AS boardId,\n" +
            "B.BOCODE AS bocode,\n" +
            "B.SMCODE AS smcode,\n" +
            "RP.STATUS AS status,\n" +
            "RP.REPLY_ID AS replyId,\n" +
            "RP.REPLY AS reply,\n" +
            "RP.MEMBER_ID AS reportMemberId,\n" +
            "RR.REPORT_ID AS reportId, " +
            "RR.MEMBER_ID AS replyMemberId, " +
            "RR.ADD_DATE AS addDate,\n" +
            "RR.REPORT_REASON AS reportReason\n" +
            "FROM TB_BOARD B, TB_REPLY RP, TB_REPLY_REPORT RR\n" +
            "WHERE B.BOARD_ID = RP.BOARD_ID\n" +
            "AND RP.REPLY_ID = RR.REPLY_ID", nativeQuery = true)
    Page<IReplyReportDto> findByReplyReport(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TB_REPLY_REPORT\n" +
            "WHERE REPORT_ID = :reportId"
            , nativeQuery = true)
    void deleteReplyReport(@Param("reportId") Long reportId);
}

package org.example.backend.repository.board;

import org.example.backend.model.dto.IReportDto;
import org.example.backend.model.entity.board.Report;
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
 * fileName : ReportRepository
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
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT B.BOARD_ID AS boardId,\n" +
            "R.REPORT_ID AS reportId, " +
            "R.MEMBER_ID AS ReMemberId,\n" +
            "B.BOARD_TITLE AS boardTitle,\n" +
            "R.REPORT_REASON AS reportReason,\n" +
            "B.STATUS AS status, " +
            "B.MEMBER_ID AS BoMemberId,\n" +
            "B.BOCODE AS bocode, " +
            "B.SMCODE AS smcode, " +
            "R.ADD_DATE AS addDate " +
            "FROM TB_BOARD B, TB_REPORT R\n" +
            "WHERE B.BOARD_ID = R.BOARD_ID",
    nativeQuery = true)
    Page<IReportDto> findByBoard(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TB_REPORT\n" +
            "WHERE REPORT_ID = :reportId"
            , nativeQuery = true)
    void deleteReport(@Param("reportId") Long reportId);
}

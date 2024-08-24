package org.example.backend.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.IReplyReportDto;
import org.example.backend.model.dto.IReportDto;
import org.example.backend.model.entity.board.Report;
import org.example.backend.repository.board.ReplyReportRepository;
import org.example.backend.repository.board.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName : org.example.backend.service.admin
 * fileName : ReportService
 * author : gumiji
 * date : 6/9/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 6/9/24         gumiji          최초 생성
 */
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReplyReportRepository replyReportRepository;

//    신고 목록 불러오기
    public Page<IReportDto> findByBoard(Pageable pageable) {
        Page<IReportDto> list = reportRepository.findByBoard(pageable);
        return list;
    }

    //  신고 삭제 (hard delete)
    public void delReport(Long reportId) {
        reportRepository.deleteReport(reportId);
    }

    //    댓글신고 목록 불러오기
    public Page<IReplyReportDto> findByReplyReport(Pageable pageable) {
        Page<IReplyReportDto> list = replyReportRepository.findByReplyReport(pageable);
        return list;
    }

    //  댓글신고 삭제 (hard delete)
    public void delReplyReport(Long reportId) {
        replyReportRepository.deleteReplyReport(reportId);
    }
}

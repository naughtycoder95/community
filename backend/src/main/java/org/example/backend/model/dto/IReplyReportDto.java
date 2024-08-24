package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : IReplyReportDto
 * author : gumiji
 * date : 6/14/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 6/14/24         gumiji          최초 생성
 */
public interface IReplyReportDto {
    Long getBoardId();

    String getBocode();

    String getSmcode();

    String getStatus();

    Long getReplyId();

    String getReply();

    Long getReportId();
    String getReportMemberId();

    String getReplyMemberId();

    String getReportReason();
    String getAddDate();
}

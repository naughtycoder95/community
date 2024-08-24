package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : IReportDto
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
public interface IReportDto {
    Long getBoardId();
    Long getReportId();
    String getReMemberId();
    String getBoardTitle();
    String getReportReason();
    String getBoMemberId();
    String getBocode();
    String getSmcode();
    String getStatus();
    String getAddDate();
}

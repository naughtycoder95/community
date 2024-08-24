package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : IMyReplyDto
 * author : sjuk2
 * date : 2024-06-12
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-12         sjuk2          최초 생성
 */
public interface IMyReplyDto {
    Long getBoardId();
    String getBocode();
    String getSmcode();
    Long getReplyId();
    String getMemberId();
    String getReply();
    String getAddDate();
}

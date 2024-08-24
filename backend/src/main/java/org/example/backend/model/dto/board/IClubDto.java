package org.example.backend.model.dto.board;

/**
 * packageName : org.example.backend.model.dto
 * fileName : IClubDto
 * author : gumiji
 * date : 5/23/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/23/24         gumiji          최초 생성
 */
public interface IClubDto {
    Long getBoardId();
    String getMemberName();
    String getBoardTitle();
    String getAddDate();
    String getCmCdName();
    String getCmCd();
    String getBocode();
    String getSmcode();
    String getReplyCount();
}

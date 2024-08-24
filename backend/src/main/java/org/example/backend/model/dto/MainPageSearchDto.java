package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : MainPageSearch
 * author : GGG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         GGG          최초 생성
 */
public interface MainPageSearchDto {
    Long getBoardId();
    String getBoardTitle();
    String getNickName();
    String getAddDate();
    String getBoCode();
    String getSmCode();
    String getMemberName();
    String getCmCode();
    String getCmCodeName();
    Integer getGood(); // 추가
    Integer getTotalReplyCount(); // 추가
}

package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : MainPageDto
 * author : GGG
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         GGG          최초 생성
 */
public interface MainPageDto {
    Long getBoardId();
    String getBoardTitle();
    String getNickName();
    Integer getGood();
    String getAddDate();
    Integer getTotalReplyCount();
    String getBoard();
    String getBoCode();
}
package org.example.backend.model.dto;

/**
 * packageName : org.example.backend.model.dto
 * fileName : MainPageArmDto
 * author : GGG
 * date : 2024-05-24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-24         GGG          최초 생성
 */
public interface MainPageArmDto {
    Long getNotifyId();
    String getMemberId();
    String getNotiContent();
    String getNotiUrl();
    String getAddDate();
    String getNotiCheck();
}

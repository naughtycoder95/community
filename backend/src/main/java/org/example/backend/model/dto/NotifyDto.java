package org.example.backend.model.dto;

import lombok.*;

/**
 * packageName : org.example.backend.model.dto
 * fileName : NotifyDto
 * author : kimtaewan
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         kimtaewan          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotifyDto {
    private String memberId;
    private String notiContent;
    private String addDate;
    private String notiUrl;
    private String notiCheck;
}

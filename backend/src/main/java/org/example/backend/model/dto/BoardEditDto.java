package org.example.backend.model.dto;

import lombok.*;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : BoardEditDto
 * author : GGG
 * date : 2024-05-30
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-30         GGG          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardEditDto {
    private String cmCd;
    private String upCmCd;
    private String cmCdName;
    private String cmCdComment;
}

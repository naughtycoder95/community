package org.example.backend.model.dto.board;

import lombok.*;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : VoteDto
 * author : kimtaewan
 * date : 2024-05-28
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-28         kimtaewan          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoteDto {
    private String voteName;    // 투표명
    private String voteList;    // 투표항목명
    private String delDate;
}

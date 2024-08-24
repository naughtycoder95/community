package org.example.backend.model.dto.board;

import lombok.*;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : BoardDto
 * author : gumiji
 * date : 5/29/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/29/24         gumiji          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDto {
    private Long boardId; // 게시글 ID 추가
    private String memberId;
    private String boardTitle;
    private String boardContent;
    private Integer good;
    private String bocode;
    private char noticeYn;
    private String smcode;
}

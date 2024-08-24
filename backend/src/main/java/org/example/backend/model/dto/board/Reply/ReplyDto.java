package org.example.backend.model.dto.board.Reply;

import jakarta.persistence.Lob;
import lombok.*;

import java.util.UUID;

/**
 * packageName : org.example.backend.model.dto.board.Reply
 * fileName : ReplyDto
 * author : SAMSUNG
 * date : 2024-06-10
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-10         SAMSUNG          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyDto {
    // Reply
    private Long replyId;       // 댓글 ID
    private Long boardId;       // 글 ID
    private String memberId;    // 회원 ID
    private String reply;       // 대댓글 내용
    private Long reReply;       // 대댓글 ID
}

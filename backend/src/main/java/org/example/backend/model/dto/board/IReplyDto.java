package org.example.backend.model.dto.board;

import org.springframework.data.jpa.repository.Query;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : IMyReplyDto
 * author : SAMSUNG
 * date : 2024-05-30
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-30         SAMSUNG          최초 생성
 */
public interface IReplyDto {
    Long getBoardId();      // 글번호
    Long getReplyId();      // 댓글 ID
    String getMemberId();   // 회원 ID
    String getMemberName();   // 이름
    String getNickname();   // 닉네임
    Long getReReply();    // 대댓글 ID
    String getReply();      // 댓글내용
    String getAddDate();    // 작성일
    String getFileUrl();    // 파일 URL
    String getFileName();    // 파일 저장 이름
}

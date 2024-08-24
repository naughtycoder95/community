package org.example.backend.model.dto.board;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : IBoardDto
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
public interface IBoardDto {
    String getMemberId();       // 회원 ID
    String getMemberName();     // 이름
    String getNickname();       // 닉네임
    Long getBoardId();          // 게시글 ID
    String getBoardTitle();     // 글제목
    String getBoardContent();   // 글내용
    char getNoticeYn();   // 글내용
    Long getGood();             // 추천수
    String getBocode();         // 게시판코드
    String getSmcode();         // 소메뉴코드
    String getAddDate();        // 등록일

}

package org.example.backend.model.dto.board;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : ISuggestBoardDto
 * author : GGG
 * date : 2024-06-05
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-05         GGG          최초 생성
 */
public interface ISuggestBoardDto {
    Long getBoardId();
    String getBoardTitle();
    String getNickName();
    String getAddDate();
    Integer getGood();
    String getReplyCount();
}

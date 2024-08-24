package org.example.backend.model.dto.board;

public interface IFreeBoardDto {
    Long getBoardId();
    String getBoardTitle();
    String getNickname();
    String getAddDate();
    Integer getGood();
    String getReplyCount();
}

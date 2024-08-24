package org.example.backend.model.dto.board;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : IUserDto
 * author : SAMSUNG
 * date : 2024-05-31
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-31         SAMSUNG          최초 생성
 */
public interface IUserDto {
    String getMemberId();
    String getMemberName();
    String getNickname();
    String getMemberCode();
    String getDeptCode();
    String getPosCode();
}

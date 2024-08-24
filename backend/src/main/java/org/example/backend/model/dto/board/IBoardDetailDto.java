package org.example.backend.model.dto.board;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : IBoardDetailDto
 * author : SAMSUNG
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         SAMSUNG          최초 생성
 */
public interface IBoardDetailDto {
    String getCmCd();           // 코드번호
    String getCmCdName();       // 코드명
    // ---------------------------------------------
    Long getBoardId();      // 게시판 ID
    String getUuid();       // 파일 ID
    String getFileUrl();    // 파일 URL
    String getFileName();    // 파일 URL


}

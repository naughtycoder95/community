package org.example.backend.model.dto.board;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

/**
 * packageName : org.example.backend.model.dto.board
 * fileName : DelBoardDto
 * author : gumiji
 * date : 6/11/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 6/11/24         gumiji          최초 생성
 */
public interface DelBoardDto {
    String getUuid();
    Long getBoardId();
    Long getReplyId();
}

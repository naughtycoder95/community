package org.example.backend.model.common;

import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

/**
 * packageName : org.example.backend.model.common
 * fileName : BoardIdMemberIdPk
 * author : SAMSUNG
 * date : 2024-06-05
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-05         SAMSUNG          최초 생성
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BoardIdMemberIdPk implements Serializable {
    private Long boardId;   // 게시판 ID
    private String memberId;    // 회원 ID
}

package org.example.backend.model.entity.board;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import org.example.backend.model.common.BoardIdMemberIdPk;

/**
 * packageName : org.example.backend.model.entity.board
 * fileName : Recommend
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
@Entity
@Table(name="TB_RECOMMEND")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BoardIdMemberIdPk.class)
public class Recommend {
    @Id
    private Long boardId;   // 게시판 ID
    @Id
    private String memberId;    // 회원 ID
}

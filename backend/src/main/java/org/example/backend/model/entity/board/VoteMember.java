package org.example.backend.model.entity.board;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.model.common.BoardIdMemberIdPk;

/**
 * packageName : org.example.backend.model.entity.board
 * fileName : VoteMember
 * author : kimtaewan
 * date : 2024-05-28
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-28         kimtaewan          최초 생성
 */
@Entity
@Table(name="TB_VOTE_MEMBER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BoardIdMemberIdPk.class)
public class VoteMember {
    @Id
    private String memberId;    // 회원 ID
    @Id
    private Long boardId;       // 게시글 ID
    private Long voteId;        // 투표 ID
}

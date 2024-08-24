package org.example.backend.model.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.model.common.BaseTimeEntity2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "TB_BOARD")
@SequenceGenerator(
        name = "TB_BOARD_SEQ_GENERATOR"
        , sequenceName = "TB_BOARD_SEQ"
        , allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_BOARD SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE BOARD_ID = ?")
public class Board extends BaseTimeEntity2 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_BOARD_SEQ_GENERATOR"
    )
    private Long boardId;           // 게시글ID pk
    private String memberId;        // 글작성자 fk
    private String boardTitle;      // 글제목
    private String boardContent;    // 글내용
    private String bocode;          // 게시판코드
    private String smcode;          // 소메뉴코드

    @Column(name = "NOTICE_YN", columnDefinition = "CHAR(1)")
    private char noticeYn;          // 공지사항여부
}

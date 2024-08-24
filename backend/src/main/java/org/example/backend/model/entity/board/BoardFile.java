package org.example.backend.model.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.model.common.BaseTimeEntity3;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "TB_BOARD_FILE")
@SequenceGenerator(
        name = "TB_BOARD_FILE_SEQ_GENERATOR"
        , sequenceName = "TB_BOARD_FILE_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_BOARD_FILE SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE FILE_BID = ?")
public class BoardFile extends BaseTimeEntity3 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_BOARD_FILE_SEQ_GENERATOR"
    )
    private Long fileBid;    // 게시판파일ID pk
    private Long boardId;   // 게시글ID fk
    private String uuid;   // 파일ID fk // 무작위 UUID를 생성후 UUID를 문자열로 반환
}

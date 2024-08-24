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

/**
 * packageName : org.example.backend.model.entity.board
 * fileName : ReplyFile
 * author : SAMSUNG
 * date : 2024-06-09
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-09         SAMSUNG          최초 생성
 */
@Entity
@Table(name = "TB_REPLY_FILE")
@SequenceGenerator(
        name = "TB_REPLY_FILE_SEQ_GENERATOR"
        , sequenceName = "TB_REPLY_FILE_SEQ"
        , allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_REPLY_FILE SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE FILE_RID = ?")
public class ReplyFile extends BaseTimeEntity3 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_REPLY_FILE_SEQ_GENERATOR"
    )
    private Long fileRid;    // 댓글파일ID (PK)
    private Long replyId;   // 댓글ID (FK)
    private String uuid;   //파일ID (FK)

}

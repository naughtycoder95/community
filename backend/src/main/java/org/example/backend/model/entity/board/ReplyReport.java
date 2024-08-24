package org.example.backend.model.entity.board;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.model.common.BaseTimeEntity3;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * packageName : org.example.backend.model.entity.board
 * fileName : ReplyReport
 * author : SAMSUNG
 * date : 2024-06-07
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-07         SAMSUNG          최초 생성
 */
@Entity
@Table(name="TB_REPLY_REPORT")
@SequenceGenerator(
        name = "TB_REPLY_REPORT_SEQ_GENERATOR"
        , sequenceName = "TB_REPLY_REPORT_SEQ"
        , allocationSize = 1
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_REPORT SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE REPORT_ID = ?")
public class ReplyReport extends BaseTimeEntity3 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_REPLY_REPORT_SEQ_GENERATOR"
    )
    private Long reportId;          // 신고 ID
    private String memberId;        // 회원 ID
    private Long replyId;           // 댓글 ID
    private String reportReason;    // 신고사유
}

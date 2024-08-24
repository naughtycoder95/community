package org.example.backend.model.entity.board;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * packageName : org.example.backend.model.entity
 * fileName : Vote
 * author : gumiji
 * date : 5/22/24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/22/24         gumiji          최초 생성
 */
@Entity
@Table(name="TB_VOTE")
@SequenceGenerator(
        name = "TB_VOTE_SEQ_GENERATOR"
        , sequenceName = "TB_VOTE_SEQ"
        , allocationSize = 1
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// soft delete
@SQLDelete(sql = "UPDATE TB_VOTE SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE VOTE_ID = ?")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "TB_VOTE_SEQ_GENERATOR"
    )
    private Long voteId;    // 투표 ID(PK)
    private Long boardId;   // 글번호
    private String voteName;    // 투표명
    private String voteList;    // 투표항목명
    private Integer voteCnt = 0;    // 투표수
    private String addDate;
    private String delDate;
    private String status;

    @PrePersist
    void OnPrePersist() {
        this.addDate = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = "Y";
    }
}

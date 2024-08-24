package org.example.backend.model.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.example.backend.model.common.BaseTimeEntity2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * packageName : org.example.backend.model.entity.auth
 * fileName : Member
 * author : sjuk2
 * date : 2024-05-22
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-22         sjuk2          최초 생성
 */
@Entity
@Table(name = "TB_MEMBER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_MEMBER SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS') WHERE MEMBER_ID = ?")
public class Member extends BaseTimeEntity2 {
    @Id
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberExt;
    private String nickname;
    private String memberCode;
    private String deptCode;
    private String posCode;
}

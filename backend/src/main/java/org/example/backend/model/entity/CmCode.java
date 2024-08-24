package org.example.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.example.backend.model.common.BaseTimeEntity3;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * packageName : org.example.backend.model
 * fileName : CmCode
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
@Table(name = "TB_CM_CODE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_CM_CODE SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS') WHERE CM_CD = ?")
public class CmCode extends BaseTimeEntity3 {
    @Id
    @Column(name = "cm_cd")
    private String cmCd;
    private String upCmCd;
    private String cmCdName;
    private String cmCdComment;
}

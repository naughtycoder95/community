package org.example.backend.model.entity.board;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.model.common.BaseTimeEntity3;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "TB_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "STATUS = 'Y'")
@SQLDelete(sql = "UPDATE TB_FILE SET STATUS = 'N', DEL_DATE = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE UUID = ?")
public class File extends BaseTimeEntity3 {
    @Id
    private String uuid;
    private String fileUrl; // 파일 URL
    private String fileName; // 파일명

    @Lob
    private byte[] data; // BLOB 타입 필드 // 파일 첨부
}

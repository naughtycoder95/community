package org.example.backend.model.dto.board;

import jakarta.persistence.Lob;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDto {
    private String uuid; // 파일ID
    private String fileUrl; // 파일 URL
    private String fileName; // 파일명
    @Lob
    private byte[] data; // BLOB 타입 필드 // 파일 첨부

}

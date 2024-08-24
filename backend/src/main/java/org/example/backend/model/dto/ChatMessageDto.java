package org.example.backend.model.dto;

import lombok.*;

/**
 * packageName : org.example.backend.model.dto
 * fileName : ChatMessageDto
 * author : kimtaewan
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         kimtaewan          최초 생성
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ChatMessageDto {
    private String memberId;
    private String nickname;
    private String content;
}

package org.example.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;



/**
 * packageName : org.example.backend.service.dto
 * fileName : LoginRequest
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
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    private String memberId;
    @NotBlank(message = "패스워드는 필수 입력 항목입니다.")
    private String memberPw;
}

package org.example.backend.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * packageName : org.example.backend.service.dto
 * fileName : SignUpRequest
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
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String memberId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String memberPw;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String memberName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 주소를 입력하세요.")
    private String memberEmail;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String memberExt;

    private String nickname;

    private String memberCode;

    private String deptCode;

    private String posCode;
}

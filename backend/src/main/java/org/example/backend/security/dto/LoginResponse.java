package org.example.backend.security.dto;

import lombok.*;

/**
 * packageName : org.example.backend.service.dto
 * fileName : LoginResponse
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
public class LoginResponse {
    private String accessToken;

    private String tokenType = "Bearer";
    private String memberId;
    private String memberCode;
    private String deptCode;

    public LoginResponse(String accessToken, String memberId, String memberCode, String deptCode) {
        this.accessToken = accessToken;
        this.memberId = memberId;
        this.memberCode = memberCode;
        this.deptCode = deptCode;
    }
}

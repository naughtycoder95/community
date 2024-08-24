package org.example.backend.security.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * packageName : org.example.backend.security.service.dto
 * fileName : LoginDto
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
public class LoginDto extends User {
    private String memberId;

    public LoginDto(String memberId, String memberPw, Collection<? extends GrantedAuthority> authorities) {
        super(memberId, memberPw, authorities);
        this.memberId = memberId;
    }
}

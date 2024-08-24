package org.example.backend.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.security.service.dto.LoginDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * packageName : org.example.backend.security.jwt
 * fileName : JwtUtils
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
@Slf4j
@Component
public class JwtUtils {

    @Value("${backend.app.jwtSecret}")
    private String jwtSecret;

    @Value("${backend.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        LoginDto loginDto = (LoginDto) authentication.getPrincipal();
        return Jwts.builder().setSubject(loginDto.getMemberId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("디지털 서명이 훼손되었습니다.: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("웹토큰이 유효하지 않습니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("웹토큰이 만료되었습니다. : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("웹토큰을 지원하지 않습니다.: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("웹토큰 내용이 비었습니다. : {}", e.getMessage());
        }

        return false;
    }
}

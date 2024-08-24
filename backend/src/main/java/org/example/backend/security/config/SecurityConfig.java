package org.example.backend.security.config;

import jakarta.servlet.DispatcherType;
import org.example.backend.security.jwt.AuthToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * packageName : org.example.backend.security.config
 * fileName : SecurityConfig
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
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthToken authenticationJwtTokenFilter() {
        return new AuthToken();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/dist/**", "/js/**", "/plugins/**",
                "/favicon.ico", "/resources/**", "/error", "/api/board/file/upload2/*"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.disable());
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(req -> req.disable());

        http.authorizeHttpRequests(req -> req
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/api/admin/**").hasAuthority("AT01")
                .requestMatchers("/api/member/**").hasAnyAuthority("AT01", "AT02")
                .requestMatchers("/api/board/**").hasAnyAuthority("AT01", "AT02")
                .requestMatchers("/api/connect/**").permitAll()
                .requestMatchers("/chat/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated());
//                .anyRequest().permitAll()); // 임시로 권한 풀기 함수
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

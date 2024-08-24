package org.example.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.repository.auth.MemberRepository;
import org.example.backend.security.service.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * packageName : org.example.backend.security.service
 * fileName : LoginDetailServiceImpl
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
@Service
@RequiredArgsConstructor
public class LoginDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException("ID 없음:" + memberId));
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(member.getMemberCode()));
        authorities.add(new SimpleGrantedAuthority(member.getDeptCode()));

        return new LoginDto(
                member.getMemberId(),
                member.getMemberPw(),
                authorities
        );
    }
}

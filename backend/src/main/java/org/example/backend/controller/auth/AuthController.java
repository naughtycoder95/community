package org.example.backend.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.security.dto.LoginRequest;
import org.example.backend.security.dto.LoginResponse;
import org.example.backend.security.dto.SignUpRequest;
import org.example.backend.security.jwt.JwtUtils;
import org.example.backend.service.auth.LoginService;
import org.example.backend.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.backend.controller.auth
 * fileName : AuthController
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
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final LoginService loginService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = loginService.authenticate(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

//        } catch (MethodArgumentNotValidException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID 또는 비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/register/{memberId}")
    public ResponseEntity<Object> reId(@PathVariable String memberId) {
        try {
            if(memberService.existById(memberId) == true) {
                return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
            } else {
                return ResponseEntity.ok("사용 가능한 ID 입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            if(memberService.existById(signUpRequest.getMemberId())) {
//                return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
                return new ResponseEntity<>("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
            }
            Member member = new Member(
                    signUpRequest.getMemberId(),
                    passwordEncoder.encode(signUpRequest.getMemberPw()),
                    signUpRequest.getMemberName(),
                    signUpRequest.getMemberEmail(),
                    signUpRequest.getMemberExt(),
                    signUpRequest.getNickname(),
                    signUpRequest.getMemberCode(),
                    signUpRequest.getDeptCode(),
                    signUpRequest.getPosCode()
            );
//            if (member.getMemberPw().isEmpty()) {
//                return ResponseEntity.badRequest().body("비밀번호를 입력해주세요.");
//            }
//            if (member.getMemberName().isEmpty()) {
//                return ResponseEntity.badRequest().body("이름을 입력해주세요.");
//            }
//            if (member.getMemberEmail().isEmpty()) {
//                return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
//            } else if (memberService.existByEmail(member.getMemberEmail()) == true) {
//                return ResponseEntity.badRequest().body("이미 사용중인 이메일입니다.");
//            }
//            if (member.getMemberExt().isEmpty()) {
//                return ResponseEntity.badRequest().body("전화번호를 입력해주세요.");
//            }
            memberService.insert(member);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("알 수 없는 오류가 발생하였습니다.");
        }
    }

    @GetMapping("/profile-id/{memberEmail}")
    public ResponseEntity<Object> findByMemberEmail(@PathVariable String memberEmail) {
        try {
            Member member = memberService.findByMemberEmail(memberEmail);

            return ResponseEntity.ok(member.getMemberId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("아이디 찾기에 실패했습니다.");
        }
    }
}

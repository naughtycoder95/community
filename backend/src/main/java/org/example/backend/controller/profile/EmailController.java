package org.example.backend.controller.profile;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.service.profile.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * packageName : org.example.backend.controller.profile
 * fileName : EmailController
 * author : sjuk2
 * date : 2024-05-23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-23         sjuk2          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PutMapping("/sendEmail/{to}/{memberId}")
    public ResponseEntity<Object> sendEmail(@PathVariable String to, @PathVariable String memberId) {
        try {
            emailService.idAndEmailCheck(to, memberId);
            emailService.sendSimpleEmail(to, memberId);
            return ResponseEntity.ok().body("이메일로 임시 비밀번호가 발송되었습니다.");
        } catch (IllegalArgumentException e) {
            log.debug("에러 발생: {}" + e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            log.debug("에러 발생: {}" + e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        if (e instanceof EntityNotFoundException) {
            log.error("404 에러 발생: {}", e);
            return new ResponseEntity<>("정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        } else {
            log.error("500 에러 발생: {}", e);
            return new ResponseEntity<>("서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

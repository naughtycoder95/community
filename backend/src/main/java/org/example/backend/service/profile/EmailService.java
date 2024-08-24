package org.example.backend.service.profile;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.repository.auth.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * packageName : org.example.backend.controller.auth
 * fileName : ChatController
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
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();
    private final ConcurrentHashMap<String, LockAndRequestTime> memberLocks = new ConcurrentHashMap<>();
    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Object sendSimpleEmail(String to, String memberId) {
        LockAndRequestTime lockAndRequestTime = memberLocks.compute(memberId, (k, v) -> {
            if (v == null || v.isRequestExpired()) {
                return new LockAndRequestTime(new ReentrantLock(), null); // 초기 발송 완료 시간은 null로 설정
            } else {
                return v;
            }
        });

        Lock lock = lockAndRequestTime.getLock();
        if (!lock.tryLock()) {
            Duration remainingTime = Duration.between(LocalDateTime.now(), lockAndRequestTime.getRequestTime().plusMinutes(1));
            throw new IllegalStateException("이미 임시 비밀번호가 이메일로 발송되었습니다. " + remainingTime.toSeconds() + "초 후에 재시도하세요.");
        }

        try {
            if (!lockAndRequestTime.isRequestExpired()) {
                return false;
            }

            String tempPw = generatePw(memberId);
            lockAndRequestTime.setRequestTime(LocalDateTime.now()); // 이메일 발송 완료 시간 기록
            memberLocks.put(memberId, lockAndRequestTime);

            String text = "임시 비밀번호 : ";
            String subject = "임시 비밀번호 발급안내.";
            String modifiedText = text + tempPw;

            MimeMessage message = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, false);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(modifiedText, true);

                mailSender.send(message);

                // 클라이언트로 응답을 반환
                ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();

                // 락은 1분간 유지
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> lock.unlock(), 1, TimeUnit.MINUTES);

                return responseEntity;
            } catch (Exception e) {
                log.debug("이메일 전송에 실패하였습니다.", e);
                throw new RuntimeException("이메일 전송에 실패하였습니다.", e);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public String generatePw(String memberId) {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String encodedPassword = passwordEncoder.encode(sb.toString());
            member.setMemberPw(encodedPassword);
            memberRepository.save(member);
        }
        return sb.toString();
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

    public void idAndEmailCheck(String to, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!member.getMemberEmail().equals(to)) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }
    }

    private static class LockAndRequestTime {
        private final Lock lock;
        private LocalDateTime requestTime;

        public LockAndRequestTime(Lock lock, LocalDateTime requestTime) {
            this.lock = lock;
            this.requestTime = requestTime;
        }

        public Lock getLock() {
            return lock;
        }

        public LocalDateTime getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(LocalDateTime requestTime) {
            this.requestTime = requestTime;
        }

        public boolean isRequestExpired() {
            return requestTime == null || LocalDateTime.now().isAfter(requestTime.plusMinutes(1));
        }
    }
}
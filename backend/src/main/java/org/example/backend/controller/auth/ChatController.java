package org.example.backend.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.dto.ChatMessageDto;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.service.auth.ChatService;
import org.example.backend.service.auth.MemberService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final MemberService memberService;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto message) {
        log.debug("메세지 발송: {}", message);
        // 아이디로 회원정보 조회
        Optional<Member> member = memberService.findById(message.getMemberId());
        // 회원정보 존재할 경우
        if (member.isPresent()) {
            // 닉네임 세팅
            message.setNickname(member.get().getNickname());
            chatService.message(message);
        } else {
            log.debug("메세지 생성에 실패했습니다.");
        }
    }
}

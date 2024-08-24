package org.example.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.model.entity.board.Board;
import org.example.backend.model.entity.board.Reply;
import org.example.backend.repository.auth.MemberRepository;
import org.example.backend.repository.board.BoardRepository;
import org.example.backend.repository.board.ReplyRepository;
import org.example.backend.service.board.BoardDetailService;
import org.example.backend.service.board.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * packageName : org.example.backend.service.auth
 * fileName : MemberService
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
public class MemberService {
    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final BoardDetailService boardDetailService;

    private final ReplyService replyService;

    private final ReplyRepository replyRepository;

    //  1) 회원 유무 확인 함수 : 회원가입
    public boolean existById(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //  이메일 중복 확인
    public boolean existByEmail(String memberEmail) {
        Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);
        if (member.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //  2) 저장/수정 : 회원가입/회원정보 변경
    public Member insert(Member member) {
        Member member2 = memberRepository.save(member);

        return member2;
    }

    //  3) 수정 : 비밀번호 변경
    public Member update(Member member) {
        member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
        Member member2 = memberRepository.save(member);

        return member2;
    }

    //  4) 회원 상세 조회
    public Optional<Member> findByMemberId(String memberId) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        return optionalMember;
    }

    //  5) email 로 회원 상세 조회 후 id 찾기
    public Member findByMemberEmail(String memberEmail) {
        Member optionalMember = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));
        return optionalMember;
    }

    //  6) 신규 회원 전체 조회
    public Page<Member> findAllByMemberCode(String memberCode, Pageable pageable) {
        Page<Member> memberList = memberRepository.findAllByMemberCode(memberCode, pageable);
        return memberList;
    }

    //  7) 기존 회원 전체 조회(검색)
    public Page<Member> findAllByMemberName(String memberName, Pageable pageable) {
        Page<Member> memberList = memberRepository.findAllByMemberName(memberName, pageable);
        return memberList;
    }

    //  8) 기존 회원 부서별 전체 조회
    public Page<Member> findAllByMemberCodeAndDeptCode(String memberCode, String deptCode, Pageable pageable) {
        Page<Member> memberList = memberRepository.findAllByMemberCodeAndDeptCode(memberCode, deptCode, pageable);
        return memberList;
    }

    //  9) 회원 삭제 (soft delete) + 게시글 삭제
    @Transactional
    public boolean removeById(String memberId) {
        if (memberRepository.existsById(memberId) == true) {
            if (boardRepository.findByMemberId(memberId) != null) {
                List<Board> deletelist = boardRepository.findByMemberId(memberId);
                for (Board board : deletelist) {
                    boardDetailService.deleteBoard(board.getBoardId());
                }
            }
//            댓글 삭제
            if (replyRepository.existsByMemberId(memberId) == true){
                List<Reply> replyList = replyRepository.findByMemberId(memberId);
                for (Reply reply : replyList) {
                    replyService.removeReply(reply.getReplyId());
                }
            }
            memberRepository.deleteById(memberId);
            return true;
        } else {
            return false;
        }
    }

    //  10) 회원 삭제 (hard delete)
    public void delMember(String memberId) {
        memberRepository.deleteMember(memberId);
    }

    //  chat
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }
}

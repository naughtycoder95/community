package org.example.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.entity.auth.Member;
import org.example.backend.service.auth.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : org.example.backend.controller.admin
 * fileName : AdminController
 * author : sjuk2
 * date : 2024-06-10
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-10         sjuk2          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    //  회원 상세 조회
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<Object> findByMemberId(@PathVariable String memberId) {
        try {
            Optional<Member> optionalMember = memberService.findByMemberId(memberId);
            if (optionalMember.isEmpty() == true) {
                // 데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // 조회 성공
                return new ResponseEntity<>(optionalMember.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  신규 회원 전체 조회
    @GetMapping("/profile-all/{memberCode}")
    public ResponseEntity<Object> findAllByMemberCode(@PathVariable String memberCode,
                                                      @PageableDefault(sort = "ADD_DATE", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<Member> memberList = memberService.findAllByMemberCode(memberCode, pageable);
            if (memberList.isEmpty() == true) {
                // 데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // 조회 성공
                return new ResponseEntity<>(memberList, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  기존 회원 전체 조회(검색)
    @GetMapping("/profile-all/old-search/{memberCode}/{page}/{size}")
    public ResponseEntity<Object> findAllByMemberName(@PathVariable String memberCode, @PathVariable int page, @PathVariable int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Member> memberPage = memberService.findAllByMemberName(memberCode, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("member", memberPage.getContent()); // member 배열
            response.put("currentPage", memberPage.getNumber()); // 현재페이지번호
            response.put("totalItems", memberPage.getTotalElements()); // 총건수(개수)
            response.put("totalPages", memberPage.getTotalPages()); // 총페이지수

            if (memberPage.isEmpty() == true) {
                // 데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // 조회 성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  기존 회원 부서별 전체 조회
    @GetMapping("/profile-all/old-dept/{memberCode}/{deptCode}/{page}/{size}")
    public ResponseEntity<Object> findAllByMemberCodeAndDeptCode(@PathVariable String memberCode, @PathVariable String deptCode, @PathVariable int page, @PathVariable int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Member> memberPage = memberService.findAllByMemberCodeAndDeptCode(memberCode, deptCode, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("member", memberPage.getContent()); // member 배열
            response.put("currentPage", memberPage.getNumber()); // 현재페이지번호
            response.put("totalItems", memberPage.getTotalElements()); // 총건수(개수)
            response.put("totalPages", memberPage.getTotalPages()); // 총페이지수

            if (memberPage.isEmpty() == true) {
                // 데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // 조회 성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  회원정보 수정
    @PutMapping("/profile-edit")
    public ResponseEntity<Object> updateProfile(
            @RequestBody Member member
    ) {
        try {
            Member member2 = memberService.insert(member); // 수정
            return new ResponseEntity<>(member2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러 (서버 에러) -> 500 신호(INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  비밀번호 수정
    @PutMapping("/profile-edit/password")
    public ResponseEntity<Object> updatePassword(
            @RequestBody Member member
    ) {
        try {
            Member member2 = memberService.update(member); // 수정
            return new ResponseEntity<>(member2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러 (서버 에러) -> 500 신호(INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  soft delete
    @DeleteMapping("/profile/deletion/{memberId}")
    public ResponseEntity<Object> delete(
            @PathVariable String memberId
    ) {
        try {
//            DB 서비스 삭제 함수 실행
            boolean success = memberService.removeById(memberId);

            if (success == true) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
//                삭제 실행 : 0건 삭제
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버(DB) 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  hard delete
    @DeleteMapping("/profile/hard-deletion/{memberId}")
    public void delMember(@PathVariable String memberId) {
        memberService.delMember(memberId);
    }
}

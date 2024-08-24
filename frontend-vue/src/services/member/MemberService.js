import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class MemberService {
  // 회원 상세 조회
  get(memberId) {
    return http.get(`/member/profile/${memberId}`, {
      headers: LoginHeader(),
    });
  }

  // 회원정보 수정
  updateProfile(data) {
    return http.put(`/member/profile-edit`, data, {
      headers: LoginHeader(),
    });
  }

  // 비밀번호 변경
  updatePassword(data) {
    return http.put(`/member/profile-edit/password`, data, {
      headers: LoginHeader(),
    });

  }
  // 회원정보 삭제
  deleteProfile(memberId) {
    return http.delete(`/member/profile/deletion/${memberId}`, {
      headers: LoginHeader(),
    });
  }

  // 본인 게시글 전체 조회
  getAllPost(memberId, page, size) {
    return http.get(`/member/activity-board/${memberId}?page=${page}&size=${size}`, {
      headers: LoginHeader(),
    });
  }

  // 본인 댓글 전체 조회
  getAllComment(memberId, page, size) {
    return http.get(`/member/activity-comment/${memberId}?page=${page}&size=${size}`, {
      headers: LoginHeader(),
    });
  }
}

export default new MemberService();

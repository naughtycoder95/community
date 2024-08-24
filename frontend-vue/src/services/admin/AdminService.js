import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class AdminService {
    // 회원 상세 조회
    get(memberId) {
        return http.get(`/admin/profile/${memberId}`, {
            headers: LoginHeader(),
          });
    }

    // 신규 회원 전체 조회
    getMember(memberCode, page, size) {
        return http.get(`/admin/profile-all/${memberCode}?page=${page}&size=${size}`, {
            headers: LoginHeader(),
          });
    }

    // 기존 회원 전체 조회(검색)
    getMemberAllDept(memberName, page, size) {
        return http.get(`/admin/profile-all/old-search/${memberName}/${page}/${size}`, {
            headers: LoginHeader(),
          });
    }

    // 기존 회원 부서별 전체 조회
    getMemberOfDept(memberCode, deptCode, page, size) {
        return http.get(`/admin/profile-all/old-dept/${memberCode}/${deptCode}/${page}/${size}`, {
            headers: LoginHeader(),
          });
    }

    // 회원정보 수정
    updateProfile(data) {
        return http.put(`/admin/profile-edit`, data, {
            headers: LoginHeader(),
          });
    }

    // 비밀번호 변경
    updatePassword(data) {
        return http.put(`/admin/profile-edit/password`, data, {
            headers: LoginHeader(),
          });
    }

    // 회원정보 삭제
    deleteProfile(memberId) {
        return http.delete(`/admin/profile/deletion/${memberId}`, {
            headers: LoginHeader(),
          });
    }

    // 회원정보 삭제(하드)
    hardDeleteProfile(memberId) {
        return http.delete(`/admin/profile/hard-deletion/${memberId}`, {
            headers: LoginHeader(),
          });
    }
}

export default new AdminService();
import http from "@/utils/http-common";

class LoginService {
    // TODO: 로그인 공통함수
    login(member) {
        let data = {
            memberId: member.memberId,
            memberPw: member.memberPw,
        };
        return http.post("/auth/login", data);
    }

    // TODO: 로그아웃 공통함수
    logout() {
        localStorage.removeItem("member");
    }

    // TODO: 회원가입 공통함수
    register(data) {
        return http.post("/auth/register", data);
    }

    // TODO: 아이디 중복확인 함수
    reId(memberId) {
        let data = {
            memberId: memberId
        };
        return http.get(`/auth/register/${memberId}`, data);
    }

    // TODO: 비밀번호 찾기 함수
    findPassword(to, memberId) {
        return http.put(`/auth/sendEmail/${to}/${memberId}`);
    }

    // TODO: email 로 회원 상세 조회 후 id 찾기
    getId(memberEmail) {
        return http.get(`/auth/profile-id/${memberEmail}`);
    }
}

export default new LoginService();
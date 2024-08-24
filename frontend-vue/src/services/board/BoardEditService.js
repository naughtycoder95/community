import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class BoardEditService {
    // 로그인된 회원 정보 조회
    getMember(memberId) {
        return http.get(`/board/board-detail/member?memberId=${memberId}`, {
            headers: LoginHeader(),
        });
    }
    // 게시글, 작성자 정보 상세 조회
    getBoard(boardId) {
        return http.get(`/board/board-detail/edit?boardId=${boardId}`, {
            headers: LoginHeader(),
        });
    }
    // 코드번호로 코드명 조회
    getCmCd(cmCd) {
        return http.get(`/board/board-detail/cmCd?cmCd=${cmCd}`, {
            headers: LoginHeader(),
        });
    }
    // 글번호로 투표 조회
    getVote(boardId) {
        return http.get(`/board/board-votes?boardId=${boardId}`, {
            headers: LoginHeader(),
        });
    }
    // 글번호로 장소 조회 (게시글 하나당 장소 하나)
    getPlace(boardId) {
        return http.get(`/board/board-detail/place?boardId=${boardId}`, {
            headers: LoginHeader(),
        });
    }
    // 글번호로 이미지 조회
    getImg(boardId) {
        // boardId를 숫자로 변환하여 전달합니다.
        const id = Number(boardId);
        return http.get(`/board/board-detail/board-img?boardId=${id}`, {
            headers: LoginHeader(),
        });
    }
}

export default new BoardEditService();

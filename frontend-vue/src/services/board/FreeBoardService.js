import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class FreeBoardService {
    // 최신글 전체조회
    getAll(boardTitle, page, size) {
        return http.get(`/board/free?boardTitle=${boardTitle}&page=${page}&size=${size}`, {
            headers: LoginHeader()
        });
    }
    // 인기글 전체조회
    getAllPopular(boardTitle, page, size) {
        return http.get(`/board/free/popular?boardTitle=${boardTitle}&page=${page}&size=${size}`, {
            headers: LoginHeader()
        });
    }
    // 공지사항 전체조회
    getNotice() {
    return http.get(`/board/free-notice?`, {
      headers: LoginHeader(),
    });
  }
}

export default new FreeBoardService();
import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class SuggestBoardService {
    // 최신글 전체조회
    getAll(boardTitle, page, size) {
        return http.get(`/board/suggest?boardTitle=${boardTitle}&page=${page}&size=${size}`, {
            headers: LoginHeader()
        });
    }
    // 공지사항 전체조회
    getNotice() {
    return http.get(`/board/suggest-notice?`, {
      headers: LoginHeader(),
    });
  }
    // 상세조회
    get(boardId) {
        return http.get(`/board/suggest/${boardId}`, {
            headers: LoginHeader()
        });
    }
    // 게시글 등록함수
    create(data) {
        return http.post("/board/suggest", data, {
            headers: LoginHeader()
        });
    }
    // 게시글 수정함수
    update(boardId, data) {
        return http.put(`/board/suggest/${boardId}`, data, {
            headers: LoginHeader()
        });
    }
    // 게시글 삭제함수
    delete(boardId) {
        return http.delete(`/board/suggest/deletion/${boardId}`, {
            headers: LoginHeader()
        });
    }
}

export default new SuggestBoardService();
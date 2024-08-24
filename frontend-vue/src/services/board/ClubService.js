import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class ClubService {
  // 동호회 전체조회
  getAll(boardTitle, bocode, page, size) {
    console.log("전체조회", boardTitle, bocode, page, size)
    return http.get(
      `/board/club?boardTitle=${boardTitle}&bocode=${bocode}&page=${page}&size=${size}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 공지사항 전체조회
  getNotice(bocode) {
    return http.get(`/board/club-notice?bocode=${bocode}`, {
      headers: LoginHeader(),
    });
  }
  // 동호회 게시판 상세조회
  get(boardId) {
    return http.get(`/board-detail/${boardId}`, {
      headers: LoginHeader(),
    });
  }
  // 중분류
  getBocode() {
    return http.get(`/board/club-bocode`, {
      headers: LoginHeader(),
    });
  }
  // 소분류
  getSmcode() {
    return http.get(`/board/club-smcode`, {
      headers: LoginHeader(),
    });
  }
}

export default new ClubService();

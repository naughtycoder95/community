import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class DeptBoardService {
  // 부서별 게시판 전체조회 - 공지글
  getNotice(smcode) {
    return http.get(`/board/dept/notice?smcode=${smcode}`, {
      headers: LoginHeader(),
    });
  }
  // 부서별 게시판 전체조회 - 일반글
  getAll(boardTitle, smcode, page, size) {
    return http.get(`/board/dept?boardTitle=${boardTitle}&smcode=${smcode}&page=${page}&size=${size}`,
      {
        headers: LoginHeader(),
      }
    );
  }
}

export default new DeptBoardService();

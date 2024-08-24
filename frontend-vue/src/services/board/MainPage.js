// services/MainPage.js
import http from '@/utils/http-common';
import LoginHeader from "../login/LoginHeader";

class MainPageService {
  // 자유게시판
  getFreeBoardData() {
    return http.get("/auth/free_board", { headers: LoginHeader() });
  }
  // 건의게시판
  getSuggestionBoardData() {
    return http.get("/auth/suggestion_board", { headers: LoginHeader() });
  }
  // 칭찬게시판
  getPraiseBoardData() {
    return http.get("/auth/praise_board", { headers: LoginHeader() });
  }

  // 핫토픽 데이터를 가져오는 함수
  getHotTopics() {
    return http.get('/auth/hot-topics', {
      headers: LoginHeader()
    });
  }
}

export default new MainPageService();
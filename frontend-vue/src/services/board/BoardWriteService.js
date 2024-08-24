import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class BoardWrite {
  // 게시물 저장
  create(data) {
    console.log("저장되나", data )
    return http.post("/board/board-write", data, {
        headers: LoginHeader(),
    });
  }

  // 게시물 수정
  update(data) {
    console.log("수정되나", data )
    return http.post("/board/board-edit", data, {
        headers: LoginHeader(),
    });
  }
}

export default new BoardWrite();
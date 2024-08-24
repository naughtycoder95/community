import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class ReplyService {
  // 글번호로 댓글 조회 + 페이징
  getReply(boardId, page, size) {
    return http.get(`/board/board-detail/reply?boardId=${boardId}&page=${page}&size=${size}`, {
      headers: LoginHeader(),
    });
  }
  // 대댓글 조회
  getReReply(boardId, replyId) {
    return http.get(
      `/board/board-detail/re-reply?boardId=${boardId}&replyId=${replyId}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 댓글 수 조회
  getReplyCount(boardId) {
    return http.get(`/board/board-detail/reply/count?boardId=${boardId}`, {
      headers: LoginHeader(),
    });
  }
  // 신고 댓글 저장
  createReplyReport(report) {
    return http.post("/board/board-detail/reply-report", report, {
      headers: LoginHeader(),
    });
  }
  // 댓글(대댓글) 저장
  createReply(temp, file, currentUrl) {
    let formData = new FormData(); // form 객체
    formData.append("boardId", temp.boardId);
    formData.append("memberId", temp.memberId);
    formData.append("reply", temp.reply);
    formData.append("reReply", temp.reReply);
    formData.append("file", file);
    formData.append("currentUrl", currentUrl);

    //formData 로그
    for (const x of formData) {
      console.log("formData ::: ", x);
    }

    return http.post("/board/board-detail/reply", formData, {
      headers: 
      {
        ...LoginHeader(),
        "Content-Type": "multipart/form-data",
      },
    });
  }
  // 댓글(대댓글) 수정
  updateReply(reply, file) {
    let formData = new FormData(); // form 객체
    formData.append("replyId", reply.replyId);
    formData.append("boardId", reply.boardId);
    formData.append("memberId", reply.memberId);
    formData.append("reply", reply.reply);
    formData.append("reReply", reply.reReply);
    formData.append("isFileDeleted", reply.isFileDeleted);
    formData.append("file", file);

    //formData 로그
    for (const x of formData) {
      console.log("formData ::: ", x);
    }
    
    return http.put(`/board/board-detail/reply`, formData, {
      headers: {
        ...LoginHeader(),
        "Content-Type": "multipart/form-data",
      },
    });
  }
  // 댓글(대댓글) 삭제
  deleteReply(replyId) {
    return http.delete(`/board/board-detail/reply/delete?replyId=${replyId}`, {
      headers: LoginHeader(),
    });
  }
}

export default new ReplyService();

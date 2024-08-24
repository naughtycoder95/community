import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class BoardDetailService {
  // 로그인된 회원 정보 조회
  getMember(memberId) {
    return http.get(`/board/board-detail/member?memberId=${memberId}`, {
      headers: LoginHeader(),
    });
  }
  // 게시글, 작성자 정보 조회
  getBoard(boardId) {
    return http.get(`/board/board-detail?boardId=${boardId}`, {
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
    return http.get(`/board/board-detail/vote?boardId=${boardId}`, {
      headers: LoginHeader(),
    });
  }
  // 투표 회원 조회
  getVoteMember(boardId, memberId) {
    return http.get(
      `/board/board-detail/vote-member?boardId=${boardId}&memberId=${memberId}`,
      {
        headers: LoginHeader(),
      }
    );
  }

  // 글번호로 장소 조회 (게시글 하나당 장소 하나)
  getPlace(boardId) {
    return http.get(`/board/board-detail/place?boardId=${boardId}`, {
      headers: LoginHeader(),
    });
  }

  // 글번호로 이미지 조회
  getImg(boardId) {
    return http.get(`/board/board-detail/board-img?boardId=${boardId}`, {
      headers: LoginHeader(),
    });
  }

  // 첨부파일 다운로드
  getFileDownload(uuid) {
    return http.get(`/board/file/upload2/${uuid}`, {
      headers: LoginHeader(),
    });
  }
  // 신고 데이터 존재 확인
  getReport(boardId, memberId) {
    return http.get(
      `/board/board-detail/report-exist?boardId=${boardId}&memberId=${memberId}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 댓글 신고 데이터 존재 확인
  getReplyReport(replyId, memberId) {
    return http.get(
      `/board/board-detail/reply-report-exist?replyId=${replyId}&memberId=${memberId}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 추천 데이터 존재 확인
  getRecommend(boardId, memberId) {
    return http.get(
      `/board/board-detail/recommend-exist?boardId=${boardId}&memberId=${memberId}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 추천 데이터 저장
  createRecommend(recommend, currentUrl) {
    let formData = new FormData();
    formData.append("boardId", recommend.boardId);
    formData.append("memberId", recommend.memberId);
    formData.append("currentUrl", currentUrl);

    for (const x of formData) {
      console.log("formData ::: ", x);
    }

    return http.post("/board/board-detail/recommend-exist", formData, {
      headers: {
        ...LoginHeader(),
        "Content-Type": "multipart/form-data",
      },
    });
  }
  // 신고글 저장
  createReport(report) {
    return http.post("/board/board-detail/report", report, {
      headers: LoginHeader(),
    });
  }
  // 투표 저장
  createVoteMember(vote) {
    return http.post("/board/board-detail/vote", vote, {
      headers: LoginHeader(),
    });
  }
  // 추천 데이터 삭제
  deleteRecommend(boardId, memberId) {
    return http.delete(
      `/board/board-detail/recommend-exist?boardId=${boardId}&memberId=${memberId}`,
      {
        headers: LoginHeader(),
      }
    );
  }
  // 추천 수 조회
  getRecommendCnt(boardId) {
    return http.get(`/board/board-detail/recommend-count?boardId=${boardId}`, {
      headers: LoginHeader(),
    });
  }
  // 게시글 삭제
  deleteBoard(boardId) {
    return http.delete(`/board/board-detail/delete/${boardId}`, {
      headers: LoginHeader(),
    });
  }
}

export default new BoardDetailService();

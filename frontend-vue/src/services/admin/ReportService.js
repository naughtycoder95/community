import http from "@/utils/http-common";
import LoginHeader from "@/services/login/LoginHeader";

class ReportService {
  // 신고글 조회
  getAll(page, size) {
    return http.get(`/admin/report?page=${page}&size=${size}`, {
      headers: LoginHeader(),
    });
  }
  // 신고댓글 조회
  getReplyAll(page, size) {
    return http.get(`/admin/reply-report?page=${page}&size=${size}`, {
      headers: LoginHeader(),
    });
  }
  // 신고 삭제(하드)
  deleteReport(reportId) {
    return http.delete(`/admin/report-deletion/${reportId}`, {
      headers: LoginHeader(),
    });
  }
  // 댓글 신고 삭제(하드)
  deleteReplyReport(reportId) {
    return http.delete(`/admin/reply-deletion/${reportId}`, {
      headers: LoginHeader(),
    });
  }
}

export default new ReportService();

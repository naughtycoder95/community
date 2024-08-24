import http from "@/utils/http-common";
import LoginHeader from "../login/LoginHeader";

class NotificationService {
        // 알림 개수 조회
  fetchNotificationCount(memberId) {
    return http.get(`/notifications/count/${memberId}`, {
      headers: LoginHeader(),
    });
  }

  // 멤버별 알림 상세 정보 조회
  getNotificationsByMemberId(memberId) {
    return http.get(`/notifications/${memberId}`, { headers: LoginHeader() });
  }

   // 알림 읽음 상태로 업데이트
  markAsRead(notifyId) {
    return http.post(`/notifications/${notifyId}/read`, {}, { headers: LoginHeader() });
  }

//   하드딜리트 함수
  deleteNotification(notifyId) {
    return http.delete(`/notifications/${notifyId}`, { headers: LoginHeader() });
  }

  // 알람 모두확인
  markAsReadAll(memberId) {
    return http.put(`/notifications/read-check/all`, { memberId }, { headers: LoginHeader() });
  }

}


export default new NotificationService();

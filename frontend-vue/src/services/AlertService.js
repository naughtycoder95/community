import http from "@/utils/http-common";
import LoginHeader from "@/services/login/LoginHeader";

class AlertService {
  update(notifyId) {
    return http.put(`/notify/read-check/${notifyId}`, {
      headers: LoginHeader(),
    });
  }
}

export default new AlertService();
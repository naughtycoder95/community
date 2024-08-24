// services/HotTopicService.js
import http from '@/utils/http-common';
import LoginHeader from "../login/LoginHeader";

class HotTopicService {
  getHotTopics() {
    return http.get('/board/hot-topics', {
      headers: LoginHeader()
    });
  }
}

export default new HotTopicService();
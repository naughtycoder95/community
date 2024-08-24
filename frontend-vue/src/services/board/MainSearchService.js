import http from '@/utils/http-common';
import LoginHeader from "../login/LoginHeader";

    class MainSearchService {
        searchBoards(boardTitle, page, size) {
          return http.get('/board/main-search', {
            params: {
              boardtitle: boardTitle,
              page: page,
              size: size,
              sort: 'addDate,desc'
            },
            headers: LoginHeader() // 헤더 추가
          });
        }
      }

export default new MainSearchService();
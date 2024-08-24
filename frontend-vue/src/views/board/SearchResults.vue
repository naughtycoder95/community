<template>
  <div class="search-results-container">
    <h2>{{ boardTitle }}에 대한 검색 결과 (총 {{ totalItems }}건)</h2>
    <table class="table">
      <thead>
        <tr>
          <th>글번호</th>
          <th>제목</th>
          <th>게시판</th>
          <th>닉네임</th>
          <th>등록일</th>
          <th>추천수</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(board, index) in boards" :key="index">
          <td>{{ board.boardId }}</td>
          <td>
            <router-link :to="generateDetailUrl(board)" class="custom-link">
              {{ board.boardTitle }} <span v-if="board.totalReplyCount">({{ board.totalReplyCount }})</span>
            </router-link>
          </td>
          <td>{{ board.boCode === board.cmCode ? board.cmCodeName : '' }}</td>
          <td>{{ board.nickName }}</td>
          <td>{{ board.addDate }}</td>
          <td>{{ board.good }}</td> <!-- 추천수 표시 -->
        </tr>
      </tbody>
    </table>
    <b-pagination
      v-model="currentPage"
      :total-rows="totalItems"
      :per-page="pageSize"
      @input="searchDate"
    ></b-pagination>
  </div>
</template>

<script>
import MainSearchService from '@/services/board/MainSearchService';

export default {
  data() {
    return {
      boards: [],
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      boardTitle: '' // 검색어 저장
    };
  },
  watch: {
    '$route.query.boardtitle': {
      immediate: true,
      handler(newVal) {
        this.boardTitle = newVal;
        this.searchDate();
      }
    },
    currentPage: {
      handler() {
        this.searchDate();
      },
      immediate: true
    }
  },
  methods: {
    async searchDate() {
      try {
        const response = await MainSearchService.searchBoards(this.boardTitle, this.currentPage - 1, this.pageSize);
        this.boards = response.data.content;
        this.totalItems = response.data.totalElements;
      } catch (error) {
        console.error("Error fetching search results:", error);
      }
    },
    generateDetailUrl(board) {
      switch (true) {
        case board.boCode.startsWith('BO02'):
          return `/board/club/${board.boCode}/${board.smCode}/${board.boardId}`;
        case board.boCode === 'BO03':
          return `/board/free/${board.boardId}`;
        case board.boCode === 'BO04':
          return `/board/suggest/${board.boardId}`;
        case board.boCode === 'BO05':
          return `/board/praise/${board.boardId}`;
        default:
          return `/board/${board.boardId}`;
      }
    }
  },
};
</script>

<style scoped>
.search-results-container {
  margin-top: 80px;
  padding: 20px;
}
.table {
  width: 100%;
  margin-top: 20px;
}

/* RouterLink 커스텀 스타일 */
.custom-link {
  color: black; /* 기본 텍스트 색상 */
  text-decoration: none; /* 밑줄 제거 */
}
</style>

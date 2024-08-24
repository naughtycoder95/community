<template>
  <div class="w-100 p-3">
    <h1 class="text-center mb-5 mt-5">자유 게시판</h1>
    <!-- 게시판 소메뉴 버튼 -->
    <div class="d-flex justify-content-center mb-5">
      <button
        class="custom-btn col-2"
        :class="{ active: selectedMenu === 'latest' }"
        @click="selectMenu('latest')"
      >
        최신순
      </button>
      <button
        class="custom-btn col-2"
        :class="{ active: selectedMenu === 'popular' }"
        @click="selectMenu('popular')"
      >
        베스트
      </button>
    </div>
    <table class="table table-hover">
      <!-- 테이블 제목 -->
      <thead class="table-light text-center">
        <tr>
          <th scope="col">글번호</th>
          <th scope="col">제목</th>
          <th scope="col">닉네임</th>
          <th scope="col">등록일</th>
          <th scope="col">추천수</th>
        </tr>
      </thead>
      <!-- 테이블 내용 (공지사항) -->
      <tbody>
        <tr
          v-for="(data, index) in freeNotice"
          :key="index"
          @click="goFreeDetail(data.boardId)"
        >
          <td class="text-center col-1">{{ data.boardId }}</td>
          <td class="col-6">
            <span class="badge text-bg-dark me-2">공지</span
            >{{ data.boardTitle }}
          </td>
          <td class="text-center col-2">{{ data.nickname }}</td>
          <td class="text-center col-2">{{ data.addDate }}</td>
          <td class="text-center col-2">{{ data.good }}</td>
        </tr>
      </tbody>
      <!-- 테이블 내용 (일반글) -->
      <tbody>
        <tr
          v-for="(data, index) in board"
          :key="index"
          @click="goFreeDetail(data.boardId)"
        >
          <td class="text-center">{{ data.boardId }}</td>
          <td>
            {{ data.boardTitle }}
            <span v-if="data.replyCount > 0" style="color: brown"
              ><i class="bi bi-chat-text"></i> [{{ data.replyCount }}]</span
            >
          </td>
          <td class="text-center">{{ data.nickname }}</td>
          <td class="text-center">{{ data.addDate }}</td>
          <td class="text-center">{{ data.good }}</td>
        </tr>
      </tbody>
    </table>
    <!-- {/* paging 시작 */} -->
    <div class="row justify-content-between">
      <div class="col-4 w-25 mb-3">
        <select
          class="form-select form-select-sm"
          v-model="pageSize"
          @change="retrieveFreeBoard()"
        >
          <option v-for="(data, index) in pageSizes" :key="index" :value="data">
            {{ data }}
          </option>
        </select>
      </div>
      <div class="col-1">
        <button
          type="button"
          class="btn btn-dark"
          style="width: 90px"
          @click="moveToFreeWrite"
        >
          글쓰기
        </button>
      </div>
    </div>
    <div class="row">
      <b-pagination
        class="col-12 mb-3 justify-content-center"
        v-model="page"
        :total-rows="count"
        :per-page="pageSize"
        @click="retrieveFreeBoard()"
      ></b-pagination>
      <!-- {/* paging 끝 */} -->
      <!-- {/* 검색어 start */} -->
      <div class="col-md-4 mx-auto">
        <div class="input-group mb-3">
          <input
            type="text"
            class="form-control"
            placeholder="검색어를 입력해주세요"
            v-model="searchBoardTitle"
            @keyup.enter="retrieveFreeBoard()"
          />
          <button
            class="btn btn-outline-secondary"
            type="button"
            @click="retrieveFreeBoard()"
          >
            검색
          </button>
        </div>
      </div>
      <!-- {/* 검색어 end */} -->
    </div>
  </div>
</template>

<script>
import FreeBoardService from "@/services/board/FreeBoardService";
export default {
  data() {
    return {
      // 백엔드 연결
      board: [],
      freeNotice: [],
      searchBoardTitle: "",
      page: 1,
      count: 0,
      pageSize: 10,

      pageSizes: [10, 25, 50],
      selectedMenu: 'latest' // 선택된 메뉴 항목을 추적하는 상태
    };
  },
  methods: {
    async retrieveFreeBoard() {
      try {
        this.retrieveFreeNotice();
        let response = await FreeBoardService.getAll(
          this.searchBoardTitle,
          this.page - 1,
          this.pageSize
        );
        const { board, totalItems } = response.data;
        this.board = board;
        this.count = totalItems;
        console.log("전체조회", response.data);
      } catch (e) {
        console.log(e);
      }
    },
    // 공지
    async retrieveFreeNotice() {
      try {
        // TODO: 비동기 코딩
        let response = await FreeBoardService.getNotice();
        this.freeNotice = response.data;
        console.log(response.data); // 웹브라우저 콘솔탬에 백앤드 데이터 표시
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    async orderByLatest() {
      try {
        this.retrieveFreeNotice(); // 공지사항 먼저 조회
        let response = await FreeBoardService.getAll(
          this.searchBoardTitle,
          this.page - 1,
          this.pageSize
        );
        const { board, totalItems } = response.data;
        this.board = board;
        this.count = totalItems;
        console.log("최신순 조회", response.data);
      } catch (e) {
        console.log(e);
      }
    },
    async orderByPopular() {
      try {
        this.retrieveFreeNotice(); // 공지사항 먼저 조회
        let response = await FreeBoardService.getAllPopular(
          this.searchBoardTitle,
          this.page - 1,
          this.pageSize
        );
        const { board, totalItems } = response.data;
        this.board = board;
        this.count = totalItems;
        console.log("인기순 조회", response.data);
      } catch (e) {
        console.log(e);
      }
    },
    // 메뉴 선택
    selectMenu(menu) {
      this.selectedMenu = menu;
      if (menu === "latest") {
        this.orderByLatest();
      } else if (menu === "popular") {
        this.orderByPopular();
      }
    },
    // TODO: 공통 페이징 함수 : select 태그
    pageSizeChange() {
      this.page = 1; // 현재패이지번호 : 1
      this.retrieveFreeBoard(); // 재조회
    },
    // 게시글 상세 페이지로 이동
    goFreeDetail(boardId) {
      this.$router.push(`/board/free/${boardId}`);
    },
    moveToFreeWrite() {
      this.$router.push("/board/free-write");
    },
  },
  mounted() {
    this.retrieveFreeBoard();
    console.log("board ::: ", this.board);
  },
};
</script>

<style scoped>
.custom-btn {
  background-color: transparent;
  border: none;
  color: #333;
  font-size: 16px;
  padding: 15px 20px;
  cursor: pointer;
  position: relative;
}

.custom-btn::after {
  /* 모든 버튼에 기본 밑줄 추가 */
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 1px;
  background-color: lightgray;
  /* 밝은 회색 밑줄 */
  transition: background-color 0.3s ease;
  /* 색상 변화 애니메이션 */
}

.custom-btn.active,
.custom-btn:hover {
  color: #000;
}

.custom-btn.active::after,
/* 활성/호버 시 밑줄 색상 변경 */
.custom-btn:hover::after {
  background-color: #000;
  /* 검정색 밑줄 */
}
</style>

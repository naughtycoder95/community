<template>
  <div class="w-100 p-3">
    <h1 class="text-center mb-5 mt-5">동호회 게시판</h1>
    <div class="d-flex justify-content-center mb-5">
      <button
        class="custom-btn col-2"
        v-for="(data, index) in bocodeName"
        :key="index"
        :class="{ active: boardCode === data.cmCd }"
        @click="pageSizeChange(data.cmCd)"
      >
        {{ data.cmCdName }}
      </button>
    </div>
  </div>
  <table class="table table-hover">
    <thead class="table-light text-center">
      <tr>
        <th scope="col">글번호</th>
        <th scope="col"></th>
        <th scope="col">제목</th>
        <th scope="col">작성자</th>
        <th scope="col">작성일</th>
      </tr>
    </thead>
    <!-- 공지사항 -->
    <tbody>
      <tr
        v-for="(data, index) in clubNotice"
        :key="index"
        @click="goBoardDetail(data.bocode, data.smcode, data.boardId)"
      >
        <td class="text-center col-1">{{ data.boardId }}</td>
        <td class="text-center col-1" style="color: lightgray">
          [{{ data.cmCdName }}]
        </td>
        <td class="col-5">
          <span class="badge text-center text-bg-dark me-2">공지</span>{{ data.boardTitle }} <span v-if="data.replyCount>0" style="color: brown"><i class="bi bi-chat-text"></i> [{{ data.replyCount }}]</span>
        </td>
        <td class="text-center col-2">{{ data.memberName }}</td>
        <td class="text-center col-2">{{ data.addDate }}</td>
      </tr>
    </tbody>
    <!-- 일반 게시글 -->
    <tbody>
      <tr v-for="(data, index) in club" :key="index" @click="goBoardDetail(data.bocode, data.smcode, data.boardId)">
        <td class="text-center col-1">{{ data.boardId }}</td>
        <td class="text-center col-1" style="color: lightgray">[{{ data.cmCdName }}]</td>
        <td class="col-5">
          {{ data.boardTitle }} <span v-if="data.replyCount>0" style="color: brown"><i class="bi bi-chat-text"></i> [{{ data.replyCount }}]</span>
        </td>
        <td class="text-center col-2">{{ data.memberName }}</td>
        <td class="text-center col-2">{{ data.addDate }}</td>
      </tr>
    </tbody>
  </table>
  <p class="text-center" v-if="!club && !clubNotice">게시물이 없습니다.</p>
  <!-- {/* paging 시작 */} -->
  <div class="row justify-content-between">
    <div class="col-4 w-25 mb-3">
      <select
        class="form-select form-select-sm"
        v-model="pageSize"
        @change="retrieveClub()"
      >
        <option v-for="(data, index) in pageSizes" :key="index" :value="data">
          {{ data }}
        </option>
      </select>
    </div>
    <div class="col-1">
      <button type="button" class="btn btn-dark" style="width: 90px" @click="goClubWrite">
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
      @click="retrieveClub()"
    ></b-pagination>
    <!-- {/* paging 끝 */} -->
    <!-- {/* 검색어 start */} -->
    <div class="col-md-4 mx-auto">
      <div class="input-group mb-3">
        <input
          type="text"
          class="form-control"
          placeholder="검색어를 입력해주세요"
          v-model="searchTitle"
          @keyup.enter="retrieveClub()"
        />
        <button
          class="btn btn-outline-secondary"
          type="button"
          @click="retrieveClub()"
        >
          검색
        </button>
      </div>
    </div>
    <!-- {/* 검색어 end */} -->
  </div>
</template>

<script>
import ClubService from "@/services/board/ClubService";
export default {
  data() {
    return {
      clubNotice: [],   // 공지사항
      club: [],         // 일반글
      searchTitle: "",  // 검색어
      bocodeName: [],   // 중메뉴
      boardId: this.$route.params.boardId,
      boardCode: this.$route.query.bocode || "BO0201",
      // 공통 페이징 속성
      page: 1, // 현재 페이지 번호
      count: 0, // 전체 데이터 개수
      pageSize: 10, // 화면에 보여질 개수

      pageSizes: [10, 20, 30], // 화면에 보여질 개수 배열
    };
  },
  methods: {
    // 동호회 게시판 출력
    async retrieveClub() {
      console.log("중메뉴 코드:", this.boardCode); // boardCode 값 출력
      try {
        this.retrieveClubNotice();
        this.retrieveBocode();
        let response = await ClubService.getAll(
          this.searchTitle,
          this.boardCode,
          this.page - 1,
          this.pageSize
        );
        const { club, totalItems } = response.data;
        this.club = club;
        this.count = totalItems;
        // 로깅
        console.log("club data:", this.club); // club 데이터 출력
        console.log("글 목록", response.data);
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    // 상세조회
    async retriveBoardId(boardId) {
      // TODO: 공통 주문 상세조회 서비스 함수 실행
      try {
        let response = await ClubService.get(boardId);
        this.boardId = response.data;
        // 로깅
        console.log(response.data); // 웹브라우저 콘솔탭
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭
      }
    },
    async retrieveClubNotice() {
      try {
        // TODO: 비동기 코딩
        let response = await ClubService.getNotice(this.boardCode);
        this.clubNotice = response.data;
        console.log(response.data); // 웹브라우저 콘솔탬에 백앤드 데이터 표시
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    // 중메뉴 조회
    async retrieveBocode() {
      try {
        // TODO: 비동기 코딩
        let response = await ClubService.getBocode();
        this.bocodeName = response.data;
        console.log(response.data); // 웹브라우저 콘솔탬에 백앤드 데이터 표시
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    // TODO: 공통 페이징 함수 : select 태그
    pageSizeChange(cmCd) {
      this.boardCode = cmCd; // 코드 저장
      this.page = 1; // 현재패이지번호 : 1
      this.retrieveClub(); // 재조회
    },
    // 게시글 상세 페이지로 이동
    goBoardDetail(bocode, smcode, boardId) {
            this.$router.push(`/board/club/${bocode}/${smcode}/${boardId}`);
        },
    goClubWrite() {
      this.$router.push("/board/club-write");
    },
  },
  mounted() {
    this.retrieveClub();
  },
};
</script>

<style>
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
  background-color: lightgray; /* 밝은 회색 밑줄 */
  transition: background-color 0.3s ease; /* 색상 변화 애니메이션 */
}

.custom-btn.active,
.custom-btn:hover {
  color: #000;
}

.custom-btn.active::after, /* 활성/호버 시 밑줄 색상 변경 */
.custom-btn:hover::after {
  background-color: #000; /* 검정색 밑줄 */
}
</style>


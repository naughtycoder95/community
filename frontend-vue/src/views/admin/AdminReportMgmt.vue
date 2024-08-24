<template>
  <div class="container">
    <AdminSidebar />
    <div class="main-content">
      <div class="header">
        <h3>신고관리</h3>
      </div>
      <div class="mt-2 mb-2">
        <input
          type="radio"
          class="btn-check me-2"
          name="options-outlined"
          id="board"
          autocomplete="off"
          @click="showReport('board')"
          checked
        />
        <label class="btn btn-outline-dark me-2" for="board">게시글</label>

        <input
          type="radio"
          class="btn-check"
          name="options-outlined"
          id="reply"
          autocomplete="off"
          @click="showReport('reply')"
        />
        <label class="btn btn-outline-dark" for="reply">댓글</label>
      </div>
      <!-- 게시글 관리 -->
      <div class="row" v-if="activeTab === 'board'">
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th class="col-1">신고자</th>
              <th class="col-1">글번호</th>
              <th class="col-3">제목</th>
              <th class="col-2">신고사유</th>
              <th class="col-2">작성자</th>
              <th class="col-3"></th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in report" :key="index">
              <td class="text-center">{{ data.reMemberId }}</td>
              <td class="text-center">{{ data.boardId }}</td>
              <td
                @click="goBoardDetail(data.bocode, data.smcode, data.boardId)"
              >
                {{ data.boardTitle }}
              </td>
              <td class="text-center">{{ data.reportReason }}</td>
              <td class="text-center" v-if="data.status === 'Y'">
                신고 확정 시 확인
              </td>
              <td class="text-center" v-if="data.status === 'N'">
                {{ data.boMemberId }}
              </td>
              <td class="text-center">
                <div v-if="data.status === 'Y'">
                  <button
                    type="button"
                    class="btn btn-secondary"
                    @click="confirmReDelete(data.reportId)"
                  >
                    취소
                  </button>
                  <button
                    class="btn btn-danger"
                    @click="confirmBoDelete(data.boardId)"
                  >
                    삭제
                  </button>
                </div>
                <div v-if="data.status === 'N'">
                  게시글 삭제 완료
                  <span
                    class="badge text-bg-secondary"
                    @click="confirmReDelete(data.reportId)"
                    >영구삭제</span
                  >
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="text-center" v-if="!report">조회된 목록이 없습니다.</div>

        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="boardPage"
          :total-rows="boardCount"
          :per-page="pageSize"
          @click="retrieveReport()"
        ></b-pagination>
      </div>
      <!-- 댓글 관리 -->
      <div class="row" v-if="activeTab === 'reply'">
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th class="col-1">신고자</th>
              <th class="col-1">글번호</th>
              <th class="col-3">내용</th>
              <th class="col-2">신고사유</th>
              <th class="col-2">작성자</th>
              <th class="col-3"></th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in replyReport" :key="index">
              <td class="text-center">{{ data.reportMemberId }}</td>
              <td class="text-center">{{ data.boardId }}</td>
              <td
                @click="goBoardDetail(data.bocode, data.smcode, data.boardId)"
              >
                {{ data.reply }}
              </td>
              <td class="text-center">{{ data.reportReason }}</td>
              <td class="text-center" v-if="data.status === 'Y'">
                신고 확정 시 확인
              </td>
              <td class="text-center" v-if="data.status === 'N'">
                {{ data.replyMemberId }}
              </td>
              <td class="text-center">
                <div v-if="data.status === 'Y'">
                  <button
                    type="button"
                    class="btn btn-secondary"
                    @click="confirmReReplyDelete(data.reportId)"
                  >
                    취소
                  </button>
                  <button
                    class="btn btn-danger"
                    @click="confirmReplyDelete(data.replyId)"
                  >
                    삭제
                  </button>
                </div>
                <div v-if="data.status === 'N'">
                  댓글 삭제 완료
                  <span
                    class="badge text-bg-secondary"
                    @click="confirmReReplyDelete(data.reportId)"
                    >영구삭제</span
                  >
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="text-center" v-if="!replyReport">
          조회된 목록이 없습니다.
        </div>

        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="replyPage"
          :total-rows="replyCount"
          :per-page="pageSize"
          @click="retrieveReplyReport()"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import BoardDetailService from "@/services/board/BoardDetailService";
import ReportService from "@/services/admin/ReportService";
import AdminSidebar from "@/components/common/AdminSidebar.vue";
import ReplyService from "@/services/board/ReplyService";

export default {
  components: {
    AdminSidebar,
  },
  data() {
    return {
      report: [],
      replyReport: [],
      boardId: this.$route.params.boardId, // 현재 글 ID 가져오기
      bocode: this.$route.params.bocode,
      smcode: this.$route.params.smcode,
      replyId: this.$route.params.replyId,

      page: 1, // 현재 페이지 번호
      count: 0, // 전체 데이터 개수
      pageSize: 10, // 화면에 보여질 개수

      boardPage: 1, // 게시글 현재 페이지 번호
      boardCount: 0, // 게시글 전체 데이터 개수
      replyPage: 1, // 댓글 현재 페이지 번호
      replyCount: 0, // 댓글 전체 데이터 개수

      activeTab: "board", // 현재 활성화된 탭 ('board' 또는 'reply')
    };
  },
  methods: {
    // 신고글 목록 가져오기
    async retrieveReport() {
      try {
        let response = await ReportService.getAll(
          this.boardPage - 1,
          this.pageSize
        );
        this.report = response.data.content;
        this.boardCount = response.data.totalElements;
        this.replyPage = 1;
        // 로깅
        console.log("report data:", this.report); // 데이터 출력
        console.log("글 목록", response.data);
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    // 신고댓글 목록 가져오기
    async retrieveReplyReport() {
      try {
        let response = await ReportService.getReplyAll(
          this.replyPage - 1,
          this.pageSize
        );
        this.replyReport = response.data.content;
        this.replyCount = response.data.totalElements;
        this.boardPage = 1;
        // 로깅
        console.log("replyReport data:", this.replyReport); // 데이터 출력
        console.log("댓글 목록", response.data);
      } catch (e) {
        console.log(e); // 웹브라우저 콘솔탭에 에러표시
      }
    },
    // 게시글/댓글 탭 전환
    showReport(type) {
      this.activeTab = type;
      if (type === "board") {
        this.retrieveReport();
      } else if (type === "reply") {
        this.retrieveReplyReport();
      }
    },
    // 게시글 삭제
    async deleteBoard(boardId) {
      try {
        let response = await BoardDetailService.deleteBoard(boardId);
        console.log("삭제", response);
        console.log("삭제", boardId);
        alert("게시글이 삭제되었습니다.");
        this.retrieveReport(); // 신고 목록을 다시 가져옴
      } catch (error) {
        console.log("삭제 에러", error);
        alert("게시글 삭제에 실패했습니다.");
      }
    },
    // 게시글 삭제 확인
    confirmBoDelete(boardId) {
      if (confirm("게시글을 삭제 하시겠습니까?")) {
        this.deleteBoard(boardId);
      }
    },
    // 게시글 신고 삭제
    async deleteReport(reportId) {
      try {
        let response = await ReportService.deleteReport(reportId);
        console.log(response);
        console.log("신고 아이디", reportId);
        alert("게시글 신고가 삭제되었습니다.");
        this.retrieveReport(); // 신고 목록을 다시 가져옴
      } catch (e) {
        console.log("에러", e);
        alert("게시글 신고 삭제가 실패하였습니다.");
      }
    },
    // 게시글 신고 삭제 확인
    confirmReDelete(reportId) {
      if (confirm("신고 내역을 삭제하시겠습니까?")) {
        this.deleteReport(reportId);
      }
    },

    // 댓글 삭제
    async deleteReply(replyId) {
      try {
        let response = await ReplyService.deleteReply(replyId);
        console.log("삭제", response);
        console.log("댓글 아이디", replyId);
        alert("댓글이 삭제되었습니다.");
        this.retrieveReplyReport(); // 신고 목록을 다시 가져옴
      } catch (error) {
        console.log("삭제 에러", error);
        alert("댓글 삭제에 실패했습니다.");
      }
    },
    // 댓글 삭제 확인
    confirmReplyDelete(replyId) {
      if (confirm("댓글을 삭제하시겠습니까?")) {
        this.deleteReply(replyId);
      }
    },

    
    // 댓글신고 삭제
    async deleteReplyReport(reportId) {
      try {
        let response = await ReportService.deleteReplyReport(reportId);
        console.log(response);
        console.log("댓글신고 아이디", reportId);
        alert("댓글 신고가 삭제되었습니다.");
        this.retrieveReplyReport(); // 댓글신고 목록을 다시 가져옴
      } catch (e) {
        console.log("에러", e);
        alert("댓글 신고 삭제가 실패하였습니다.");
      }
    },
    // 댓글신고 삭제 확인
    confirmReReplyDelete(reportId) {
      if (confirm("댓글 신고 내역을 삭제하시겠습니까?")) {
        this.deleteReplyReport(reportId);
      }
    },
    // 게시글로 이동
    goBoardDetail(bocode, smcode, boardId) {
      if (bocode === "BO03") {
        this.$router.push(`/board/free/${boardId}`);
      } else if (bocode === "BO04") {
        this.$router.push(`/board/suggest/${boardId}`);
      } else if (bocode === "BO05") {
        this.$router.push(`/board/praise/${boardId}`);
      } else if (bocode === "BO01") {
        this.$router.push(`/board/dept/${smcode}/${boardId}`);
      } else {
        this.$router.push(`/board/club/${bocode}/${smcode}/${boardId}`);
      }
    },
  },
  mounted() {
    this.retrieveReport();
  },
};
</script>

<style scoped>
.container {
  margin-top: 50px;
  display: flex;
  font-family: "Arial, sans-serif";
  height: 130vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.profile-link {
  color: white;
  text-decoration: none;
}

.profile-main {
  color: black;
  text-decoration: none;
}

.main-content {
  flex-grow: 1;
  padding: 40px;
  border-radius: 15px;
  margin: 0 20px;
  border-radius: 10px;
  border: 1px solid #959595;
}

button {
  color: white;
  border: none;
  padding: 10px 20px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.horizontal-text {
  white-space: nowrap;
}
</style>

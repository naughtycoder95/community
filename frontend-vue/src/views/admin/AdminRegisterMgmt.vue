<template>
  <div class="container">
    <AdminSidebar />
    <div class="main-content">
      <div class="header">
        <h3>회원가입관리</h3>
      </div>
      <div class="mt-2 mb-2">
        <input
          type="radio"
          class="btn-check me-2"
          name="options-outlined"
          id="board"
          autocomplete="off"
          @click="showReport('register')"
          checked
        />
        <label class="btn btn-outline-dark me-2" for="board">가입신청</label>

        <input
          type="radio"
          class="btn-check"
          name="options-outlined"
          id="reply"
          autocomplete="off"
          @click="showReport('deny')"
        />
        <label class="btn btn-outline-dark" for="reply">반려회원</label>
      </div>
      <div class="row" v-if="activeTab === 'register'">
        <!-- 가입승인 -->
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">이름</th>
              <th scope="col">이메일</th>
              <th scope="col">번호</th>
              <th scope="col">닉네임</th>
              <th scope="col">부서</th>
              <th scope="col">직급</th>
              <th scope="col">승인</th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in member" :key="index">
              <td class="col-1 text-center">{{ data.memberId }}</td>
              <td class="col-1 text-center">{{ data.memberName }}</td>
              <td class="col-1 text-center">{{ data.memberEmail }}</td>
              <td class="col-1 text-center">{{ data.memberExt }}</td>
              <td class="col-1 text-center">{{ data.nickname }}</td>
              <td class="col-1 text-center">{{ deptName(data.deptCode) }}</td>
              <td class="col-1 text-center">{{ posName(data.posCode) }}</td>
              <td class="col-1 text-center">
                <div class="button-container">
                  <button class="approve-button" @click="confirmApprove(data)">
                    <span class="horizontal-text">승인</span>
                  </button>
                  <button class="reject-button" @click="confirmReject(data)">
                    <span class="horizontal-text">보류</span>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <p class="text-center" v-if="!member">조회된 목록이 없습니다.</p>
        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="memberPage"
          :total-rows="memberCount"
          :per-page="pageSize"
          @click="getAllProfile()"
        ></b-pagination>
      </div>
      <div class="row" v-if="activeTab === 'deny'">
        <!-- 반려회원 -->
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">이름</th>
              <th scope="col">이메일</th>
              <th scope="col">번호</th>
              <th scope="col">닉네임</th>
              <th scope="col">부서</th>
              <th scope="col">직급</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in denyMember" :key="index">
              <td class="col-1 text-center">{{ data.memberId }}</td>
              <td class="col-1 text-center">{{ data.memberName }}</td>
              <td class="col-1 text-center">{{ data.memberEmail }}</td>
              <td class="col-1 text-center">{{ data.memberExt }}</td>
              <td class="col-1 text-center">{{ data.nickname }}</td>
              <td class="col-1 text-center">{{ deptName(data.deptCode) }}</td>
              <td class="col-1 text-center">{{ posName(data.posCode) }}</td>
              <td class="col-1 text-center">
                <div class="button-container">
                  <button
                    type="button"
                    class="btn btn-primary"
                    data-bs-toggle="modal"
                    :data-bs-target="'#editModal-' + index"
                  >
                    <span class="horizontal-text">수정</span>
                  </button>
                  <button class="delete-button" @click="confirmDelete(data)">
                    <span class="horizontal-text">삭제</span>
                  </button>
                </div>
                <!-- Modal -->
                <div
                  class="modal fade"
                  :id="'editModal-' + index"
                  tabindex="-1"
                >
                  <form>
                    <div class="modal-dialog modal-dialog-centered">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h1 class="modal-title fs-5" id="modalLabel">
                            회원정보 수정
                          </h1>
                          <button
                            type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"
                          ></button>
                        </div>
                        <div class="modal-body text-start">
                          <h3 class="fs-5 mb-2">ID</h3>
                          <input disabled
                            class="form-control mb-3"
                            v-model="data.memberId"
                          />
                          <h3 class="fs-5 mb-2">이름</h3>
                          <input
                            class="form-control mb-3"
                            v-model="data.memberName"
                          />
                          <h3 class="fs-5 mb-2">이메일</h3>
                          <input
                            class="form-control mb-3"
                            v-model="data.memberEmail"
                          />
                          <h3 class="fs-5 mb-2">번호</h3>
                          <input
                            class="form-control mb-3"
                            v-model="data.memberExt"
                          />
                          <h3 class="fs-5 mb-2">닉네임</h3>
                          <input
                            class="form-control mb-3"
                            v-model="data.nickname"
                          />
                          <h3 class="fs-5 mb-2">부서</h3>
                          <select
                            class="form-select mb-3"
                            v-model="data.deptCode"
                          >
                            <option value="DE01">영업팀</option>
                            <option value="DE02">인사팀</option>
                            <option value="DE03">행정팀</option>
                            <option value="DE04">보안팀</option>
                          </select>
                          <h3 class="fs-5 mb-2">직급</h3>
                          <select
                            class="form-select mb-3"
                            v-model="data.posCode"
                          >
                            <option value="PO01">사원</option>
                            <option value="PO02">주임</option>
                            <option value="PO03">대리</option>
                            <option value="PO04">과장</option>
                          </select>
                        </div>
                        <div class="modal-footer">
                          <button
                            type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal"
                          >
                            닫기
                          </button>
                          <button
                            type="submit"
                            class="btn btn-primary"
                            @click="registerModify(data)"
                          >
                            등록
                          </button>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <p class="text-center" v-if="!denyMember">조회된 목록이 없습니다.</p>
        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="denyMemberPage"
          :total-rows="denyMemberCount"
          :per-page="pageSize"
          @click="getAllDenyProfile()"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import AdminService from "@/services/admin/AdminService";
import AdminSidebar from "@/components/common/AdminSidebar.vue";
export default {
  components: {
    AdminSidebar,
  },
  data() {
    return {
      member: [],
      denyMember: [],
      activeTab: "register",

      page: 1, // 현재 페이지 번호
      count: 0, // 전체 데이터 개수
      pageSize: 10, // 화면에 보여질 개수

      memberPage: 1, // 게시글 현재 페이지 번호
      memberCount: 0, // 게시글 전체 데이터 개수
      denyMemberPage: 1, // 댓글 현재 페이지 번호
      denyMemberCount: 0, // 댓글 전체 데이터 개수
    };
  },
  methods: {
    // 게시글/댓글 탭 전환
    showReport(type) {
      this.activeTab = type;
      if (type === "register") {
        this.getAllProfile();
      } else if (type === "deny") {
        this.getAllDenyProfile();
      }
    },
    // 회원가입 목록
    async getAllProfile() {
      try {
        let response = await AdminService.getMember(
          "AT03",
          this.memberPage - 1,
          this.pageSize
        );
        this.member = response.data.content;
        this.memberCount = response.data.totalElements;
        this.denyMemberPage = 1;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },
    // 반려회원 목록
    async getAllDenyProfile() {
      try {
        let response = await AdminService.getMember(
          "AT04",
          this.denyMemberPage - 1,
          this.pageSize
        );
        this.denyMember = response.data.content;
        this.denyMemberCount = response.data.totalElements;
        this.memberPage = 1;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },
    async registerApprove(data) {
      try {
        data.memberCode = "AT02";
        let response = await AdminService.updateProfile(data);
        console.log(response.data);
        alert("회원가입이 승인되었습니다.");
        this.getAllProfile();
      } catch (e) {
        console.log(e);
      }
    },
    async registerReject(data) {
      try {
        data.memberCode = "AT04";
        let response = await AdminService.updateProfile(data);
        console.log(response.data);
        alert("회원가입이 보류되었습니다.");
        this.getAllProfile();
      } catch (e) {
        console.log(e);
      }
    },
    // 회원정보 수정
    async registerModify(data) {
      try {
        data.memberCode = "AT02";
        let response = await AdminService.updateProfile(data);
        console.log(response.data);
        alert("회원정보가 변경되었습니다.");
      } catch (e) {
        console.log(e);
      }
    },
    // 회원 삭제
    async registerDelete(data) {
      let response = await AdminService.hardDeleteProfile(data.memberId);
      console.log(response.data);
      alert("회원 삭제 처리되었습니다.");
      this.getAllDenyProfile();
    },
    deptName(deptCode) {
      if (deptCode === "DE01") {
        return "영업팀";
      } else if (deptCode === "DE02") {
        return "인사팀";
      } else if (deptCode === "DE03") {
        return "행정팀";
      } else if (deptCode === "DE04") {
        return "보안팀";
      } else {
        return deptCode;
      }
    },
    posName(posCode) {
      if (posCode === "PO01") {
        return "사원";
      } else if (posCode === "PO02") {
        return "주임";
      } else if (posCode === "PO03") {
        return "대리";
      } else if (posCode === "PO04") {
        return "과장";
      } else {
        return posCode;
      }
    },
    //  가입신청 승인
    confirmApprove(memberId) {
      if (confirm(`회원가입을 승인하시겠습니까?`)) {
        this.registerApprove(memberId);
      }
    },
    //  가입신청 보류
    confirmReject(memberId) {
      if (confirm(`회원가입을 보류하시겠습니까?`)) {
        this.registerReject(memberId);
      }
    },
    //  반려회원 삭제
    confirmDelete(memberId) {
      if (confirm(`회원을 삭제하시겠습니까?`)) {
        this.registerDelete(memberId);
      }
    },
  },

  mounted() {
    this.getAllProfile();
    this.getAllDenyProfile();
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

.delete-button {
  background-color: #dc3545;
  color: white;
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

.approve-button {
  background-color: #4caf50;
}

.approve-button:hover {
  background-color: #45a049;
}

.reject-button {
  background-color: #ff9800;
}

.reject-button:hover {
  background-color: #e68900;
}

.button-container {
  display: flex;
  flex-direction: row;
  gap: 10px;
}
.horizontal-text {
  white-space: nowrap;
}
</style>

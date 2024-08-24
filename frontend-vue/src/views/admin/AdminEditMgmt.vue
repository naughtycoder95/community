<template>
  <div class="container">
    <AdminSidebar />
    <div class="main-content">
      <div class="header">
        <h3>사원관리</h3>
      </div>
      <br />
      <input
        type="text"
        class="search-input"
        placeholder="검색"
        v-model="searchMemberName"
        @keyup.enter="getAllProfileSearch"
      />
      <button class="search-button" @click="getAllProfileSearch">검색</button>
      <button class="dept-button" @click="pageSizeChange('DE01')">
        영업팀
      </button>
      <button class="dept-button" @click="pageSizeChange('DE02')">
        인사팀
      </button>
      <button class="dept-button" @click="pageSizeChange('DE03')">
        행정팀
      </button>
      <button class="dept-button" @click="pageSizeChange('DE04')">
        보안팀
      </button>
      <div class="row">
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
            <tr v-for="(data, index) in member" :key="index">
              <td class="col-1 text-center">{{ data.memberId }}</td>
              <td class="col-1 text-center">{{ data.memberName }}</td>
              <td class="col-1 text-center">{{ data.memberEmail }}</td>
              <td class="col-1 text-center">{{ data.memberExt }}</td>
              <td class="col-1 text-center">{{ data.nickname }}</td>
              <td class="col-1 text-center">{{ deptName(data.deptCode) }}</td>
              <td class="col-1 text-center">{{ posName(data.posCode) }}</td>
              <td class="col-1 text-center">
                <div class="button-group">
                  <button
                    type="button"
                    class="edit-button btn btn-primary"
                    data-bs-toggle="modal"
                    :data-bs-target="'#editModal-' + index"
                  >
                    <span class="horizontal-text">수정</span>
                  </button>
                  <button class="delete-button" @click="registerDelete(data)">
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
                        <div class="modal-body">
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
      </div>
      <p class="text-center" v-if="!member">조회된 목록이 없습니다.</p>
      <b-pagination
        class="col-12 mb-3 justify-content-center"
        v-model="page"
        :total-rows="count"
        :per-page="pageSize"
        @click="getAllProfileOfDept"
      ></b-pagination>
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
      allMember: [],
      searchMemberName: "",
      deptCode: "DE01",
      page: 1,
      count: 0,
      pageSize: 10,
      boardId: this.$route.params.boardId,
    };
  },
  methods: {
    async getAllProfileSearch() {
      try {
        let response = await AdminService.getMemberAllDept(
          this.searchMemberName,
          this.page - 1,
          this.pageSize
        );
        const { member, totalItems } = response.data;
        this.member = member;
        this.count = totalItems;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },
    async getAllProfileOfDept() {
      try {
        let response = await AdminService.getMemberOfDept(
          "AT02",
          this.deptCode,
          this.page - 1,
          this.pageSize
        );
        const { member, totalItems } = response.data;
        this.member = member;
        this.count = totalItems;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },
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
    async registerDelete(data) {
      let response = await AdminService.deleteProfile(data.memberId);
      console.log(response.data);
      alert("회원 삭제 처리되었습니다.");
      this.getAllProfileOfDept();
    },
    pageSizeChange(dept) {
      this.page = 1;
      this.deptCode = dept;
      this.getAllProfileOfDept();
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
  },
  mounted() {
    this.getAllProfileOfDept();
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

.search-input {
  flex-grow: 1;
  padding: 10px 15px;
  border: 2px solid #b3000f;
  border-radius: 5px 0 0 5px;
  outline: none;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  border-color: #8b0000;
}

.search-button {
  background-color: #b3000f;
  padding: 10px 20px;
  border: 2px solid #b3000f;
  border-left: none;
  border-radius: 0 5px 5px 0;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #8b0000;
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

.dept-button {
  background-color: black;
}

.delete-button {
  background-color: #f44336;
}

.delete-button:hover {
  background-color: #d32f2f;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.horizontal-text {
  display: inline-block;
  text-align: center;
  white-space: nowrap;
}
</style>

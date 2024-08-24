<template>
  <div class="container">
    <AdminSidebar />
    <div class="main-content">
      <div class="header">
        <h3>게시판목록</h3>
        <p>게시판을 생성/수정/삭제합니다.</p>
      </div>
      <div class="controls">
        <span class="total-count">총 게시판 수: {{ boards.length }}</span>
        <button class="add-board-button" @click="toggleAddBoardForm">게시판 추가</button>
      </div>
      <div v-if="showForm" class="form-container">
        <input v-model="newBoardCd" placeholder="게시판 코드" />
        <input v-model="newBoardUpCmCd" placeholder="상위 게시판 코드 (선택사항)" />
        <input v-model="newBoardName" placeholder="게시판명" />
        <input v-model="newBoardComment" placeholder="게시판설명" />
        <button @click="addBoard">추가</button>
        <button @click="cancelEdit">취소</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
      <div v-if="showEditForm" class="form-container">
        <input v-model="editBoardData.cmCd" placeholder="게시판 코드" disabled />
        <input v-model="editBoardData.upCmCd" placeholder="상위 게시판 코드 (선택사항)" />
        <input v-model="editBoardData.cmCdName" placeholder="게시판명" />
        <input v-model="editBoardData.cmCdComment" placeholder="게시판설명" />
        <button @click="updateBoard">저장</button>
        <button @click="cancelEdit">취소</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
      <div class="table-container">
        <table class="board-table">
          <thead>
            <tr>
              <th>번호</th>
              <th>게시판 코드</th>
              <th>상위 게시판 코드</th>
              <th>게시판명</th>
              <th>게시판설명</th>
              <th>상태</th>
              <th>기능</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(board, index) in boards" :key="index">
              <td>{{ index + 1 }}</td>
              <td>{{ board.cmCd }}</td>
              <td>{{ board.upCmCd }}</td>
              <td>{{ board.cmCdName }}</td>
              <td>{{ board.cmCdComment }}</td>
              <td>{{ board.status === 'Y' ? '활성화' : '비활성화' }}</td>
              <td>
                <div class="button-group">
                  <button class="edit-button" @click="editBoard(board)">수정</button>
                  <button v-if="board.status === 'Y'" class="deactivate-button" @click="confirmDeactivate(board.cmCd)">비활성화</button>
                  <button v-else class="reactivate-button" @click="changeStatus(board.cmCd, 'Y')">활성화</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import AdminSidebar from "@/components/common/AdminSidebar.vue";
import BoardManageService from "@/services/board/BoardManageService";

export default {
  components: {
    AdminSidebar
  },
  data() {
    return {
      boards: [], // 게시판 데이터를 담을 배열
      showForm: false,
      newBoardCd: '',
      newBoardName: '',
      newBoardComment: '',
      newBoardUpCmCd: '',
      showEditForm: false,
      editBoardData: {
        cmCd: '',
        upCmCd: '',
        cmCdName: '',
        cmCdComment: ''
      },
      errorMessage: '' // 에러 메시지
    };
  },
  methods: {
    async fetchBoards() {
      try {
        const response = await BoardManageService.getAll();
        this.boards = response.data;
      } catch (error) {
        console.error('Error fetching boards:', error);
      }
    },
    toggleAddBoardForm() {
      this.showForm = !this.showForm;
    },
    async addBoard() {
      this.errorMessage = ''; // 에러 메시지 초기화
      try {
        await BoardManageService.postBoard(this.newBoardCd, this.newBoardName, this.newBoardComment, this.newBoardUpCmCd);
        this.showForm = false; // 폼을 숨김
        this.newBoardCd = '';
        this.newBoardName = '';
        this.newBoardComment = '';
        this.newBoardUpCmCd = '';
        this.fetchBoards(); // 새로운 게시판을 추가한 후 게시판 목록을 다시 불러옴
      } catch (error) {
        console.error('Error adding board:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data; // 에러 메시지 설정
        } else {
          this.errorMessage = '게시판 추가 중 오류가 발생했습니다.';
        }
      }
    },
    editBoard(board) {
      this.cancelEdit();
      this.editBoardData = { ...board };
      this.showEditForm = !this.showEditForm;
    },
    async updateBoard() {
      try {
        await BoardManageService.updateBoard(
          this.editBoardData.cmCd,
          this.editBoardData.cmCdName,
          this.editBoardData.cmCdComment,
          this.editBoardData.upCmCd
        );
        this.showEditForm = false;
        this.fetchBoards();
      } catch (error) {
        console.error('Error updating board:', error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data; // 에러 메시지 설정
        } else {
          this.errorMessage = '게시판 수정 중 오류가 발생했습니다.';
        }
      }
    },
    async deleteBoard(cmCd) {
      try {
        if (confirm("정말 삭제하시겠습니까? 하위 게시판이 있을 경우 하위 게시판 모두 비활성화 됩니다.")) {
          await BoardManageService.deleteBoard(cmCd);
          this.fetchBoards();
        }
      } catch (error) {
        console.error('Error deleting board:', error);
        alert('게시판 삭제 중 오류가 발생했습니다.');
      }
    },
    async changeStatus(cmCd, status) {
      try {
        if (status === 'Y') {
          await BoardManageService.reactivateBoard(cmCd);
        } else {
          await BoardManageService.deleteBoard(cmCd);
        }
        this.fetchBoards();
      } catch (error) {
        console.error('Error changing board status:', error);
        alert('상태 변경 중 오류가 발생했습니다.');
      }
    },
    confirmDeactivate(cmCd) {
      if (confirm("정말 비활성화하시겠습니까? 하위 게시판이 있을 경우 하위 게시판 모두 비활성화 됩니다.")) {
        this.changeStatus(cmCd, 'N');
      }
    },
    cancelEdit() {
      this.showEditForm = false;
      this.showForm = false;
      this.editBoardData = {
        cmCd: '',
        upCmCd: '',
        cmCdName: '',
        cmCdComment: ''
      };
    }
  },
  mounted() {
    this.fetchBoards(); // 컴포넌트가 마운트될 때 게시판 데이터를 가져옴
  }
};
</script>

<style scoped>
.container {
  margin-top: 50px;
  display: flex;
  font-family: "Arial, sans-serif";
  height: 130vh;
}

.main-content {
  flex-grow: 1;
  padding: 40px;
  border-radius: 15px;
  margin: 0 20px;
  border-radius: 10px;
  border: 1px solid #959595;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.total-count {
  font-weight: bold;
}

.add-board-button {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.board-table th,
.board-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
  white-space: nowrap; /* 텍스트를 가로로 표시 */
}

.board-table th {
  background-color: #f4f4f4;
}

.manage-button,
.edit-button,
.delete-button,
.deactivate-button,
.reactivate-button {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  margin-right: 5px;
  border-radius: 5px;
  white-space: nowrap; /* 버튼 내부 텍스트를 가로로 표시 */
}

.manage-button {
  background-color: #17a2b8;
  color: white;
}

.edit-button {
  background-color: #ffc107;
  color: white;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.deactivate-button {
  background-color: #6c757d;
  color: white;
}

.reactivate-button {
  background-color: #28a745;
  color: white;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

.error-message {
  color: red;
  margin-top: 10px;
}

.table-container {
  max-height: 60vh; /* 테이블 최대 높이 설정 */
  overflow-y: auto; /* 스크롤바 추가 */
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px; /* 버튼 간격 조정 */
}

.button-group button {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  white-space: nowrap; /* 버튼 내부 텍스트를 가로로 표시 */
}
</style>

<template>
  <div class="container">
    <div class="sidebar">
      <h1 class="sidebar-title">{{ member.memberName }}님</h1>
      <hr class="sidebar-divider" />
      <ul class="sidebar-list">
        <router-link to="/profile" class="profile-link"
          ><li class="sidebar-menu mb-5">회원정보</li></router-link
        >
        <router-link to="/profile-edit/password" class="profile-link"
          ><li class="sidebar-menu mb-5">비밀번호변경</li></router-link
        >
        <router-link to="/profile-edit/nickname" class="profile-link"
          ><li class="sidebar-menu mb-5">닉네임변경</li></router-link
        >
        <router-link to="/profile-activity" class="profile-link"
          ><li class="sidebar-menu">활동내역</li></router-link
        >
      </ul>
    </div>
    <div class="main-content">
      <h3>활동내역</h3>
      <hr />
      <button class="activity-button" @click="showBoard">내 작성글 보기</button>
      <button class="activity-button" @click="showComment">내 작성댓글 보기</button>

      <div class="row" v-if="boardVisible">
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th scope="col">글번호</th>
              <th scope="col">제목</th>
              <th scope="col">등록일</th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in board" :key="index">
              <td class="col-1 text-center">{{ data.boardId }}</td>
              <td class="col-7 text-center" @click="goBoardDetail(data.bocode, data.smcode, data.boardId)">{{ data.boardTitle }}</td>
              <td class="col-2 text-center">{{ data.addDate }}</td>
            </tr>
          </tbody>
        </table>
        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="boardPage"
          :total-rows="boardCount"
          :per-page="boardPageSize"
          @click="retrieveBoard"
        ></b-pagination>
      </div>

      <div class="row" v-if="replyVisible">
        <table class="table">
          <thead class="table-light text-center">
            <tr>
              <th scope="col">글번호</th>
              <th scope="col">댓글번호</th>
              <th scope="col">내용</th>
              <th scope="col">등록일</th>
            </tr>
          </thead>
          <tbody class="table-group-divider align-middle">
            <tr v-for="(data, index) in reply" :key="index">
              <td class="col-1 text-center">{{ data.boardId }}</td>
              <td class="col-1 text-center">{{ data.replyId }}</td>
              <td class="col-7 text-center" @click="goBoardDetail(data.bocode, data.smcode, data.boardId)">{{ data.reply }}</td>
              <td class="col-2 text-center">{{ data.addDate }}</td>
            </tr>
          </tbody>
        </table>
        <b-pagination
          class="col-12 mb-3 justify-content-center"
          v-model="replyPage"
          :total-rows="replyCount"
          :per-page="replyPageSize"
          @click="retrieveComment"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import MemberService from "@/services/member/MemberService";

export default {
  data() {
    return {
      member: {},
      message: "",

      board: [],
      reply: [],

      boardPage: 1,
      boardCount: 0,
      boardPageSize: 10,

      replyPage: 1,
      replyCount: 0,
      replyPageSize: 10,

      boardVisible: false,
      replyVisible: false,
    };
  },
  methods: {
    async getProfile() {
      try {
        let response = await MemberService.get(
          this.$store.state.member?.memberId
        );
        this.member = response.data;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },

    async retrieveBoard() {
      try {
        let response = await MemberService.getAllPost(
          this.$store.state.member?.memberId,
          this.boardPage - 1,
          this.boardPageSize
        );
        const { board, totalItems } = response.data;
        this.board = board;
        this.boardCount = totalItems;
        console.log(response.data);
      } catch (e) {
        console.log(e);
      }
    },

    async retrieveComment() {
      try {
        let response = await MemberService.getAllComment(
          this.$store.state.member?.memberId,
          this.replyPage - 1,
          this.replyPageSize
        );
        const { reply, totalItems } = response.data;
        this.reply = reply;
        this.replyCount = totalItems;
        console.log("ㅁㄴㅇ", response.data);
      } catch (e) {
        console.log(e);
      }
    },

    showBoard() {
      this.boardVisible = true;
      this.replyVisible = false;
      this.boardPage = 1;
      this.retrieveBoard();
    },

    showComment() {
      this.boardVisible = false;
      this.replyVisible = true;
      this.replyPage = 1;
      this.retrieveComment();
    },

    goBoardDetail(bocode, smcode, boardId) {
      if (bocode === "BO01") {
        this.$router.push(`/board/dept/${smcode}/${boardId}`);
      } else if (bocode === "BO0201" || bocode === "BO0202" || bocode === "BO0203" || bocode === "BO0204") {
        this.$router.push(`/board/club/${bocode}/${smcode}/${boardId}`);
      } else if (bocode === "BO03") {
        this.$router.push(`/board/free/${boardId}`);
      } else if (bocode === "BO04") {
        this.$router.push(`/board/suggest/${boardId}`);
      } else if (bocode === "BO05") {
        this.$router.push(`/board/praise/${boardId}`);
      }
    },
  },
  mounted() {
    this.getProfile();
    this.showBoard();
  },
};
</script>

<style scoped>
.container {
  margin-top: 50px;
  height: 100vh;
  display: flex;
  font-family: "Arial, sans-serif";
}

.profile-link {
  color: white;
  text-decoration: none;
}

.profile-main {
  color: black;
  text-decoration: none;
}

.sidebar {
  width: 220px;
  background: #b3000f;
  color: white;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 15px;
}

.sidebar-title {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.sidebar-divider {
  border: 0;
  height: 1px;
  background: white;
  margin: 20px 0;
}

.sidebar-list {
  list-style: none;
  padding: 0;
  width: 100%;
}

.sidebar-menu {
  font-size: 18px;
  padding: 10px;
  cursor: pointer;
  transition: background 0.3s;
}

.sidebar-menu:hover {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
}

.main-content {
  flex-grow: 1;
  padding: 40px;
  background-color: #f9f9f9;
  border-radius: 15px;
  margin: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.activity-button {
  color: white;
  background-color: black;
  border: none;
  padding: 10px 20px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}
</style>

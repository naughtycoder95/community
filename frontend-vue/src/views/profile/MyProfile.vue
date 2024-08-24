<template>
  <div class="container">
    <div class="sidebar">
      <h1 class="sidebar-title">{{ member.memberName }}님</h1>
      <hr class="sidebar-divider" />
      <ul class="sidebar-list">
        <router-link to="/profile" class="profile-link"><li class="sidebar-menu mb-5">회원정보</li></router-link>
        <router-link to="/profile-edit/password" class="profile-link"><li class="sidebar-menu mb-5">비밀번호변경</li></router-link>
        <router-link to="/profile-edit/nickname" class="profile-link"><li class="sidebar-menu mb-5">닉네임변경</li></router-link>
        <router-link to="/profile-activity" class="profile-link"><li class="sidebar-menu">활동내역</li></router-link>
      </ul>
    </div>
    <div class="main-content">
      <div class="section">
        <h3>회원정보</h3>
        <hr />
        <div class="user-info">
          <div class="details">
            <div class="detail-item">
              <span class="label">이름</span>
              <span class="label">{{ member.memberName }}</span>
            </div>
            <div class="detail-item">
              <span class="label">이메일</span>
              <span class="label">{{ member.memberEmail }}</span>
            </div>
            <div class="detail-item">
              <span class="label">번호</span>
              <span class="label">{{ member.memberExt }}</span>
            </div>
            <div class="detail-item">
              <span class="label">닉네임</span>
              <span class="label">{{ member.nickname }}</span>
            </div>
            <div class="detail-item">
              <span class="label">부서</span>
              <span class="label">{{ deptName }}</span>
            </div>
            <div class="detail-item">
              <span class="label">직급</span>
              <span class="label">{{ posName }}</span>
            </div>
          </div>
        </div>
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
    };
  },
  computed: {
    deptName() {
      if (this.member.deptCode === "DE01") {
        return "영업팀";
      } else if (this.member.deptCode === "DE02") {
        return "인사팀";
      } else if (this.member.deptCode === "DE03") {
        return "행정팀";
      } else if (this.member.deptCode === "DE04") {
        return "보안팀";
      } else {
        return this.member.deptCode;
      }
    },
    posName() {
      if (this.member.posCode === "PO01") {
        return "사원";
      } else if (this.member.posCode === "PO02") {
        return "주임";
      } else if (this.member.posCode === "PO03") {
        return "대리";
      } else if (this.member.posCode === "PO04") {
        return "과장";
      } else {
        return this.member.posCode;
      }
    },
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
  },
  mounted() {
    this.getProfile();
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

.section h3 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.details {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.detail-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 18px;
  font-weight: 500;
}

.edit {
  display: flex;
  justify-content: center;
}

.edit-link {
  color: white;
  text-decoration: none;
}

.edit-button {
  background-color: #b3000f;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s;
  display: block;
  margin: 0;
}

.edit-button:hover {
  background-color: #80000b;
}
</style>

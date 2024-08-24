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
        <h3>비밀번호 변경</h3>
        <hr />
        <div class="user-info">
          <div class="details">
            <div class="detail-item">
              <span class="label">비밀번호</span>
              <input
                class="edit-box"
                type="password"
                placeholder="비밀번호 입력"
                v-model="password"
              />
            </div>
            <div class="detail-item">
              <span class="label">비밀번호 확인</span>
              <input
                class="edit-box"
                type="password"
                placeholder="비밀번호 재입력"
                v-model="rePw" />
            </div>
            <div v-if="!isPasswordMatch">
              비밀번호가 일치하지 않습니다.
            </div>
          </div>
          <button class="edit-button" @click="editProfile">수정</button>
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
      password: "",
      rePw: "",
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
    async editProfile() {
      try {
        let data = {
          memberId: this.member.memberId,
          memberPw: this.password,
          memberName: this.member.memberName,
          memberEmail: this.member.memberEmail,
          memberExt: this.member.memberExt,
          nickname: this.member.nickname,
          memberCode: this.member.memberCode,
          deptCode: this.member.deptCode,
          posCode: this.member.posCode,
        };
        let response = await MemberService.updatePassword(data);
        console.log(response.data);
        alert("회원정보가 변경되었습니다.");
        this.$router.push("/profile");
      } catch (e) {
        console.log(e);
      }
    },
  },
  computed: {
    isPasswordMatch() {
      return this.password === this.rePw;
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
  background: #b3000f; /* #b3000f 색상 */
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

.edit-box {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  outline: none;
  transition: border-color 0.3s, box-shadow 0.3s;
  width: 70%;
}

.edit-box:focus {
  border-color: #b3000f;
  box-shadow: 0 0 5px rgba(179, 0, 15, 0.5);
}

.edit-button {
  background-color: #b3000f; /* #b3000f 색상 */
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  color: white;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s;
  display: block;
  margin: 20px auto 0;
}

.edit-button:hover {
  background-color: #80000b; /* #b3000f 색상의 어두운 버전 */
}
</style>

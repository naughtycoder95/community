<template>
    <div class="sidebar">
      <h1 class="sidebar-title">{{ loginMember.memberName }}님</h1>
      <hr class="sidebar-divider" />
      <ul class="sidebar-list">
        <router-link to="/admin/board-mgmt" class="profile-link">
          <li class="sidebar-menu mb-5">게시판관리</li>
        </router-link>
        <router-link to="/admin/edit-mgmt" class="profile-link">
          <li class="sidebar-menu mb-5">사원관리</li>
        </router-link>
        <router-link to="/admin/report-mgmt" class="profile-link">
          <li class="sidebar-menu mb-5">신고관리</li>
        </router-link>
        <router-link to="/admin/register-mgmt" class="profile-link">
          <li class="sidebar-menu mb-5">회원가입관리</li>
        </router-link>
      </ul>
    </div>
  </template>
  
  <script>
  import AdminService from "@/services/admin/AdminService";
  
  export default {
    data() {
      return {
        loginMember: {}
      };
    },
    async created() {
      try {
        const response = await AdminService.get(this.$store.state.member?.memberId);
        this.loginMember = response.data;
      } catch (e) {
        console.log(e);
      }
    }
  };
  </script>
  
  <style scoped>
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
  
  .profile-link {
    color: white;
    text-decoration: none;
  }
  </style>
  
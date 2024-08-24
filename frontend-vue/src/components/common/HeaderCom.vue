<template>
  <header class="header">
    <div class="header-top">
      <div class="header-left">
        <router-link to="/" class="custom-logo">
          <h1>SM Company</h1>
        </router-link>
      </div>
      <div class="header-center">
        <div class="search-box">
          <input
            type="text"
            placeholder="검색어를 입력하세요"
            class="search-input"
            v-model="searchQuery"
            @keydown.enter="executeSearch"
          />
          <button @click="executeSearch" class="search-button">
            <i class="bi bi-search"></i>
          </button>
        </div>
      </div>
      <div class="header-right">
        <div
          v-if="isLoggedIn"
          class="notification-icon"
          @click="toggleNotifications"
        >
          <i class="bi bi-bell"></i>
          <span class="notification-count">{{ notificationCount }}</span>
        </div>
        <router-link to="/profile" v-if="isLoggedIn" class="profile-icon">
          <i class="bi bi-person-circle"></i>
        </router-link>
        <a><router-link to="/login" v-if="!isLoggedIn">로그인</router-link></a>
        <a
          ><router-link to="/register" v-if="!isLoggedIn"
            >회원가입</router-link
          ></a
        >
        <a href="/" @click.prevent="handleLogout" v-if="isLoggedIn">로그아웃</a>
        <a v-if="isLoggedIn && memberCode === 'AT01'"
          ><router-link to="/admin/board-mgmt">관리자 페이지</router-link></a
        >
      </div>
    </div>
    <nav class="header-nav">
      <ul>
        <li>
          <a
            @click.prevent="checkLoginAndNavigate('/board/dept')"
            class="router-link nav-link"
            >부서 게시판</a
          >
        </li>
        <li>
          <a
            @click.prevent="checkLoginAndNavigate('/board/club')"
            class="router-link nav-link"
            >동호회 게시판</a
          >
        </li>
        <li>
          <a
            @click.prevent="checkLoginAndNavigate('/board/free')"
            class="router-link nav-link"
            >자유 게시판</a
          >
        </li>
        <li>
          <a
            @click.prevent="checkLoginAndNavigate('/board/suggest')"
            class="router-link nav-link"
            >건의 게시판</a
          >
        </li>
        <li>
          <a
            @click.prevent="checkLoginAndNavigate('/board/praise')"
            class="router-link nav-link"
            >칭찬 게시판</a
          >
        </li>
      </ul>
    </nav>
    <!-- 알림 팝업 -->
    <div v-if="showNotifications" class="notifications-popup">
      <div class="notification-bar">
        <h5>알림</h5>
        <button class="mark-all-read" @click="markAllAsRead">모두 확인</button>
      </div>
      <div
        v-for="notification in notifications"
        :key="notification.notifyId"
        class="notification-item"
      >
        <p @click="handleNotificationClick(notification)">
          {{ notification.notiContent }}
        </p>
        <div class="notification-footer">
          <small>{{ notification.addDate }}</small>
          <button @click.stop="deleteNotification(notification.notifyId)">
            X
          </button>
        </div>
      </div>
    </div>
    <!-- 채팅창 -->
    <div class="chat-container" v-if="isLoggedIn && showChat">
      <div class="chat-header">
        <button @click="toggleChat">닫기</button>
      </div>
      <div class="chat-messages">
        <div v-for="message in messages" :key="message.id" class="chat-message">
          <strong>{{ message.nickname }}:</strong> {{ message.content }}
        </div>
      </div>
      <div class="chat-input">
        <input type="text" v-model="newMessage" @keyup.enter="sendMessage" />
        <button @click="sendMessage">전송</button>
      </div>
    </div>
    <button
      class="chat-toggle-button"
      @click="toggleChat"
      v-if="isLoggedIn && !showChat"
    >채팅 <i class="bi bi-chat-dots"></i> 
    </button>
  </header>
</template>

<script>
import NotificationService from "@/services/board/NotificationService";
import LoginService from "@/services/login/LoginService";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
const stompClient = Stomp.over(new SockJS("http://localhost:9000/chat"));

export default {
  data() {
    return {
      notificationCount: 0,
      notifications: [],
      showNotifications: false,
      searchQuery: "",
      newMessage: "",
      messages: [],
      stompClient: null,
      showChat: false,

      badWords: ["ㅅㅂ", "ㅂㅅ", "욕설", "바보", "멍청이", "미친"],
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.loggedIn;
    },
    memberCode() {
      return this.$store.state.member
        ? this.$store.state.member.memberCode
        : null;
    },
  },
  watch: {
    isLoggedIn(LoggedIn) {
      if (LoggedIn) {
        this.fetchNotificationsCount();
        this.fetchNotifications();
      }
    },
    'newMessage': function (newValue) {
      this.newMessage = this.filterBadWords(newValue);
    },
  },
  methods: {
    checkLoginAndNavigate(path) {
      if (this.isLoggedIn) {
        this.$router.push(path);
      } else {
        alert("로그인이 필요합니다.");
      }
    },
    connect() {
      stompClient.connect(
        {},
        (frame) => {
          console.log("Connected: " + frame);
          stompClient.subscribe("/topic/message", (message) => {
            const receivedMessage = JSON.parse(message.body);
            console.log("Received: ", receivedMessage); // 로그 추가
            this.messages.push(receivedMessage);
          });
        },
        (error) => {
          console.error("Error connecting: " + error);
        }
      );
    },
    disconnect() {
      if (stompClient) {
        stompClient.disconnect();
      }
    },
    sendMessage() {
      if (stompClient && this.newMessage.trim()) {
        const message = {
          memberId: this.$store.state.member.memberId,
          content: this.newMessage.trim(),
        };
        stompClient.send("/app/message", JSON.stringify(message));
        this.newMessage = "";
      }
    },
    async fetchNotificationsCount() {
      try {
        const response = await NotificationService.fetchNotificationCount(
          this.$store.state.member.memberId
        );
        this.notificationCount = response.data;
      } catch (error) {
        console.error("Error fetching notification count:", error);
      }
    },
    async fetchNotifications() {
      try {
        const response = await NotificationService.getNotificationsByMemberId(
          this.$store.state.member.memberId
        );
        this.notifications = response.data;
      } catch (error) {
        console.error("Error fetching notifications:", error);
      }
    },
    async deleteNotification(notifyId) {
      try {
        await NotificationService.deleteNotification(notifyId);
        this.notifications = this.notifications.filter(
          (n) => n.notifyId !== notifyId
        );
        await this.fetchNotificationsCount();
      } catch (error) {
        console.error("Error deleting notification:", error);
      }
    },
    async handleNotificationClick(notification) {
      try {
        await NotificationService.markAsRead(notification.notifyId);
        window.location.href = notification.notiUrl;
      } catch (error) {
        console.error("Error marking notification as read:", error);
      }
    },
    async markAllAsRead() {
      try {
        await NotificationService.markAsReadAll(
          this.$store.state.member.memberId
        );
        await this.fetchNotifications();
        await this.fetchNotificationsCount();
      } catch (error) {
        console.error("Error marking all notifications as read:", error);
      }
    },
    toggleNotifications() {
      this.showNotifications = !this.showNotifications;
    },
    handleLogout() {
      LoginService.logout();
      this.$store.commit("logout");
      this.$router.push("/login");
    },
    toggleChat() {
      this.showChat = !this.showChat;
    },
    executeSearch() {
      if (this.searchQuery.trim()) {
        this.$router.push({
          path: "/board/main-search",
          query: { boardtitle: this.searchQuery.trim() },
        });
      }
    },
        // 나쁜 단어 필터링
        filterBadWords(text) {
      this.badWords.forEach((word) => {
        if (text.includes(word)) {
          alert(`"${word}"은(는) 입력할 수 없습니다.`);
          text = text.replace(new RegExp(word, "gi"), "");
        }
      });
      return text;
    },
  },
  mounted() {
    if (this.isLoggedIn) {
      this.fetchNotificationsCount();
      this.fetchNotifications();
      this.connect();
    }
  },
  beforeUnmount() {
    this.disconnect();
  },
};
</script>

<style scoped>
@import "@/assets/css/home.css";

.chat-container {
  z-index: 1000;
  position: fixed;
  bottom: 0;
  right: 0;
  width: 300px;
  height: 400px;
  border: 1px solid #ccc;
  background: #fff;
  display: flex;
  flex-direction: column;
}

.chat-header {
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background: #f1f1f1;
  border-bottom: 1px solid #ccc;
}

.chat-messages {
  z-index: 1000;
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.chat-message {
  z-index: 1000;
  margin-bottom: 10px;
}

.chat-input {
  z-index: 1000;
  display: flex;
  border-top: 1px solid #ccc;
  padding: 10px;
}

.chat-input input {
  z-index: 1000;
  flex: 1;
  padding: 5px;
}

.chat-input button {
  z-index: 1000;
  padding: 5px 10px;
}

.chat-toggle-button {
  z-index: 1000;
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 10px 20px;
  background: #000000;
  color: #ffffff;
  cursor: pointer;
  border-radius: 10px;
}

.profile-icon {
  margin-left: 25px;
  font-size: 1.5em; /* 원하는 크기로 설정 */
}

.nav-link {
  cursor: pointer;
}
</style>

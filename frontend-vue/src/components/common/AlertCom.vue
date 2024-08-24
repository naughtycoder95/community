<template>
  <div>
    <div v-if="showPopup" class="popup">
      <div v-if="currentNotification">
        {{ currentNotification.notiContent }}
      </div>
      <div class="popup-footer">
        <button @click="openNotificationLink">확인</button>
        <button @click="closePopup">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";

export default {
  data() {
    return {
      showPopup: false,
      popupTimeoutId: null,
    };
  },
  computed: {
    ...mapState(["loggedIn", "member"]),
    ...mapGetters(["nextNotification"]),
    currentNotification() {
      return this.nextNotification;
    },
  },
  watch: {
    loggedIn(newValue) {
      if (newValue) {
        this.connectSSE();
      } else {
        this.disconnectSSE();
      }
    },
    currentNotification(newNotification) {
      if (newNotification && !this.showPopup) {
        this.showNextNotification();
      }
    },
  },
  methods: {
    ...mapActions(["connectSSE", "disconnectSSE", "updateNotification"]),
    showNextNotification() {
      if (this.currentNotification) {
        this.showPopup = true;
        this.setPopupTimeout();
      }
    },
    openNotificationLink() {
      if (this.currentNotification) {
        const notiUrl = this.currentNotification.notiUrl;
        this.$router.push(notiUrl);
        this.closePopup();
      }
    },
    closePopup() {
      this.showPopup = false;
      this.clearPopupTimeout();
      this.$store.commit("removeNotification");
    },
    setPopupTimeout() {
      this.popupTimeoutId = setTimeout(() => {
        this.closePopup();
      }, 10000); // 10초 후에 팝업을 자동으로 닫습니다. (원하는 시간으로 변경 가능)
    },
    clearPopupTimeout() {
      if (this.popupTimeoutId) {
        clearTimeout(this.popupTimeoutId);
        this.popupTimeoutId = null;
      }
    },
  },
  mounted() {
    if (this.loggedIn) {
      this.connectSSE();
    }
  },
  beforeUnmount() {
    if (this.loggedIn) {
      this.disconnectSSE();
    }
    this.clearPopupTimeout();
  },
};
</script>

<style scoped>
.popup {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.popup-footer {
  margin-top: 10px;
  text-align: center;
}

.popup-footer button {
  margin-right: 10px;
}
</style>

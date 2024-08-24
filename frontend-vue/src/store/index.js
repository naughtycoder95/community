import { createStore } from 'vuex';
import { NativeEventSource, EventSourcePolyfill } from 'event-source-polyfill';
import LoginHeader from "@/services/login/LoginHeader";
const EventSource = NativeEventSource || EventSourcePolyfill;

const member = JSON.parse(localStorage.getItem("member"));

export default createStore({
  state: {
    loggedIn: !!member, // 로그인 여부
    member: member || null, // 로그인 정보(웹토큰 속성 있음)
    eventSource: null,
    notifications: [],
    retryCount: 0,
  },
  getters: {
    nextNotification(state) {
      return state.notifications.length > 0 ? state.notifications[0] : null;
    }
  },
  mutations: {
    loginSuccess(state, member) {
      state.loggedIn = true;
      state.member = member;
    },
    loginFailure(state) {
      state.loggedIn = false;
      state.member = null;
    },
    logout(state) {
      state.loggedIn = false;
      state.member = null;
      if (state.eventSource) {
        state.eventSource.close();
        state.eventSource = null;
      }
    },
    registerSuccess(state) {
      state.loggedIn = false;
    },
    registerFailure(state) {
      state.loggedIn = false;
    },
    addNotification(state, notification) {
      state.notifications.push(notification);
      console.log('Notification added:', notification);
    },
    removeNotification(state) {
      state.notifications.shift();
    },
    setEventSource(state, eventSource) {
      state.eventSource = eventSource;
    },
    incrementRetryCount(state) {
      state.retryCount++;
    },
    resetRetryCount(state) {
      state.retryCount = 0;
    }
  },
  actions: {
    connectSSE({ state, commit, dispatch }) {
      if (state.member && !state.eventSource) {
        const eventSource = new EventSource(
          `http://localhost:9000/api/connect/${state.member.memberId}`,
          { headers: LoginHeader() },
          { timeout: 3600000 }
        );

        eventSource.addEventListener('connect', (event) => {
          console.log('연결확인:', event.data);
        });

        eventSource.addEventListener('notification', (event) => {
          console.log('메세지 수신:', event.data);
          const notifyData = JSON.parse(event.data);
          commit('addNotification', notifyData);
        });

        eventSource.onerror = (error) => {
          console.error('EventSource failed:', error);

          if (state.retryCount <= 10) {
            setTimeout(() => {
              dispatch('connectSSE');
            }, 5000);
            commit('incrementRetryCount');
          } else {
            console.error('재시도 한도 초과.');
          }

          commit('setEventSource', null);
        };

        commit('setEventSource', eventSource);
        commit('resetRetryCount');
      }
    },
    disconnectSSE({ state, commit }) {
      if (state.eventSource) {
        state.eventSource.close();
        commit('setEventSource', null);
      }
    },
  },
  modules: {
  }
});

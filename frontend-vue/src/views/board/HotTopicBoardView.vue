<template>
    <div class="w-100 p-3">
      <h1 class="text-center mb-5 mt-5">핫토픽 게시판</h1>
      <table class="table table-hover">
        <thead class="table-light text-center">
          <tr>
            <th scope="col">글번호</th>
            <th scope="col">제목</th>
            <th scope="col">닉네임</th>
            <th scope="col">작성일</th>
            <th scope="col">추천수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(data, index) in hotTopics" :key="index" @click="goHotTopicDetail(data.boardId)">
            <td class="text-center">{{ data.boardId }}</td>
            <td>
              {{ data.boardTitle }} <span v-if="data.totalReplyCount">({{ data.totalReplyCount }})</span>
            </td>
            <td class="text-center">{{ data.nickName }}</td>
            <td class="text-center">{{ data.addDate }}</td>
            <td class="text-center">{{ data.good }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import HotTopicService from "@/services/board/HotTopicService";
  
  export default {
    data() {
      return {
        hotTopics: [],
      };
    },
    methods: {
      async fetchHotTopics() {
        try {
          const response = await HotTopicService.getHotTopics();
          this.hotTopics = response.data.slice(0, 30); // 30개만 표시
        } catch (error) {
          console.error("Error fetching hot topics:", error);
        }
      },
      goHotTopicDetail(boardId) {
        this.$router.push(`/board/free/${boardId}`);
      },
    },
    mounted() {
      this.fetchHotTopics();
    },
  };
  </script>
  
  <style scoped>
  .table-hover tbody tr:hover {
    background-color: #f5f5f5;
  }
  </style>
  
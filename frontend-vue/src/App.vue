<template>
  <!-- Header -->
  <HeaderCom />
  <!-- 본문 -->
  <div class="container d-flex flex-column min-vh-100">
    <router-view/>
    <!-- AlertCom -->
  <AlertCom/>
   </div>

  <!-- Footer -->
  <FooterCom />
</template>

<script>
import HeaderCom from "./components/common/HeaderCom.vue";
import FooterCom from "./components/common/FooterCom.vue";
import AlertCom from "./components/common/AlertCom.vue";

export default {
  components: {
    HeaderCom,
    FooterCom,
    AlertCom
  }
}
</script>

<style>
</style>
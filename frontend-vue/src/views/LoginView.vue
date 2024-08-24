<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-xl-10 col-lg-12 col-md-9">
        <div class="card mt-5">
          <div class="card-body p-0">
            <!-- {/* Nested Row within Card Body */} -->
            <div class="row justify-content-center">
              <div class="col-lg-7">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 text-gray-900 mb-4">로그인</h1>
                  </div>
                  <!-- 사용법 : @submit.prevent="함수" -->
                  <!-- prevent : submit 의 기본 속성(다른 곳으로 이동) 막기 -->
                  <Form
                    class="user"
                    @submit="handleLogin"
                    :validation-schema="schema"
                  >
                    <div class="form-group">
                      <Field
                        type="text"
                        class="form-control form-control-user mb-3"
                        placeholder="ID 입력"
                        name="memberId"
                        v-model="member.memberId"
                      />
                      <ErrorMessage
                        name="memberId"
                        class="badge text-bg-danger mb-4"
                      />
                    </div>
                    <div class="form-group">
                      <Field
                        type="password"
                        class="form-control form-control-user mb-3"
                        placeholder="패스워드 입력"
                        name="memberPw"
                        v-model="member.memberPw"
                      />
                      <ErrorMessage
                        name="memberPw"
                        class="badge text-bg-danger mb-4"
                      />
                    </div>

                    <button class="btn btn-primary btn-user w-100 mb-3">
                      로그인
                    </button>
                  </Form>
                  <div v-if="errorMessage" class="alert alert-danger mt-3">
                    {{ errorMessage }}
                  </div>
                  <hr />
                  <div class="text-center">
                    <a class="small" href="/find-id"> ID 찾기 </a>
                  </div>
                  <div class="text-center">
                    <a class="small" href="/find-password"> 비밀번호 찾기 </a>
                  </div>
                  <div class="text-center">
                    <a class="small" href="/register"> 회원가입 </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import LoginService from "@/services/login/LoginService";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    return {
      member: {
        memberId: "",
        memberPw: "",
        memberCode: "",
      },
      errorMessage: "",
    };
  },
  computed: {
    // yup : 검증 스키마 정의
    schema() {
      return yup.object({
        memberId: yup.string().required("ID를 입력해 주세요"),
        memberPw: yup.string().required("비밀번호를 입력해 주세요"),
      });
    },
  },
  methods: {
    // 함수 정의
    async handleLogin() {
      try {
        let response = await LoginService.login(this.member);
        console.log("로그인 성공", response.data);
        localStorage.setItem("member", JSON.stringify(response.data));
        this.$store.commit("loginSuccess", response.data);
        if (response.data.memberCode == "AT04") {
          LoginService.logout();
          this.$store.commit("logout");
          alert("회원가입이 반려되었습니다. 관리자에게 문의하세요.");
          this.$router.push("/login");
        } else if (response.data.memberCode == "AT03") {
          LoginService.logout();
          this.$store.commit("logout");
          alert("승인 대기 중입니다.");
          this.$router.push("/login");
        } else {
          this.$router.push("/");
        }
      } catch (e) {
        this.$store.commit("loginFailure");
        this.errorMessage = "로그인에 실패했습니다. 다시 시도해 주세요.";
        console.log(e.response.data);
      }
    },
  },

  // TODO: 화면이 뜰때 실행되는 함수
  mounted() {
    if (this.$store.state.loggedIn == true) {
      this.$router.push("/");
    }
  },
};
</script>
<style></style>

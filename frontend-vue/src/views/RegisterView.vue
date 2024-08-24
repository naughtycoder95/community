<template>
  <div>
    <div class="card mt-5">
      <div class="card-body p-0">
        <!-- Nested Row within Card Body -->
        <div class="row justify-content-center">
          <div class="col-lg-7">
            <div class="p-5">
              <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">회원가입</h1>
              </div>
              <Form
                class="user"
                @submit="handleRegister"
                :validation-schema="schema"
              >
                <div class="form-group row">
                  <div class="col-sm-9 mb-3 mb-sm-0">
                    <Field
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="아이디 입력"
                      name="memberId"
                      v-model="member.memberId"
                    />
                    <ErrorMessage
                      name="memberId"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="col-sm-3 mb-3 mb-sm-0" align="right">
                    <input
                      type="button"
                      id="idcheck"
                      class="btn btn-secondary"
                      @click="checkMemberId"
                      value="중복확인"
                    />
                  </div>
                </div>
                <div class="form-group row">
                  <div class="col-sm-6 mb-3 mb-sm-0">
                    <Field
                      type="password"
                      class="form-control form-control-user mb-3"
                      placeholder="비밀번호 입력"
                      name="memberPw"
                      v-model="member.memberPw"
                    />
                    <ErrorMessage
                      name="memberPw"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="col-sm-6">
                    <Field
                      type="password"
                      class="form-control form-control-user mb-3"
                      id="exampleRepeatPassword"
                      placeholder="비밀번호 재확인"
                      name="rePw"
                      v-model="member.rePw"
                    />
                    <ErrorMessage
                      name="rePw"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="form-group">
                    <Field
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="이름 입력"
                      name="memberName"
                      v-model="member.memberName"
                    />
                    <ErrorMessage
                      name="memberName"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="form-group">
                    <Field
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="이메일 입력"
                      name="memberEmail"
                      v-model="member.memberEmail"
                    />
                    <ErrorMessage
                      name="memberEmail"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="form-group">
                    <Field
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="번호 입력"
                      name="memberExt"
                      v-model="member.memberExt"
                    />
                    <ErrorMessage
                      name="memberExt"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div class="form-group">
                    <Field
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="닉네임 입력"
                      name="nickname"
                      v-model="member.nickname"
                    />
                    <ErrorMessage
                      name="nickname"
                      class="badge text-bg-danger mb-4"
                    />
                  </div>
                  <div>
                    <p>부서분류</p>
                  </div>
                  <div class="form-group">
                    <select
                      class="form-select form-control-select form-select-sm mb-3"
                      name="deptCode"
                      v-model="member.deptCode"
                    >
                      <option value="DE01">영업</option>
                      <option value="DE02">인사</option>
                      <option value="DE03">행정</option>
                      <option value="DE04">보안</option>
                    </select>
                  </div>
                  <div>
                    <p>직급분류</p>
                  </div>
                  <div class="form-group">
                    <select
                      class="form-select form-control-select form-select-sm mb-3"
                      name="deptCode"
                      v-model="member.posCode"
                    >
                      <option value="PO01">사원</option>
                      <option value="PO02">주임</option>
                      <option value="PO03">대리</option>
                      <option value="PO04">과장</option>
                    </select>
                  </div>
                </div>
                <button
                  type="submit"
                  class="btn btn-primary btn-user w-100 mb-3"
                >
                  등록
                </button>
              </Form>
              <p v-if="message" class="alert alert-primary" role="alert">
                {{ message }}
              </p>
              <p v-if="errorMessage" class="alert alert-danger" role="alert">
                {{ errorMessage }}
              </p>
              <hr />
              <div class="text-center">
                <a href="/login"> 로그인 </a>
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
        rePw: "",
        memberName: "",
        memberEmail: "",
        memberExt: "",
        nickname: "",
        memberCode: "AT03",
        deptCode: "DE01",
        posCode: "PO01",
      },
      message: "",
      errorMessage: "",
    };
  },
  // TODO: 함수 정의
  methods: {
    async checkMemberId() {
      try {
        this.successMessage = "";
        let response = await LoginService.reId(this.member.memberId);
        this.message = "사용가능한 ID 입니다.";
        return response.data;
      } catch (e) {
        this.message = e.response.data;
        console.log(e);
      }
    },
    async handleRegister() {
      this.message = ""; // 성공 메세지 초기화
      try {
        let temp = {
          memberId: this.member.memberId,
          memberPw: this.member.memberPw,
          memberExt: this.member.memberExt,
          memberName: this.member.memberName,
          memberEmail: this.member.memberEmail,
          memberCode: this.member.memberCode,
          deptCode: this.member.deptCode,
          posCode: this.member.posCode,
          nickname: this.member.nickname,
        };
        // TODO: 공통 사용자 등록 서비스 함수 실행
        let response = await LoginService.register(temp);
        // 공유 저장소의 register 성공 함수 실행
        this.$store.commit("registerSuccess");
        this.errorMessage = "";
        this.message = "사용자가 등록되었습니다";
        this.$router.push("/login");
        // 로깅
        console.log(response.data);
      } catch (e) {
        // 공유 저장소의 register 실패 함수 실행
        // this.$store.commit("registerFailure");
        this.message = "";
        this.errorMessage = this.handleAxiosError(e);
        console.log(e); // 에러 출력
      }
    },
    handleAxiosError(error) {
      if (error.response) {
        return error.response.data;
      }
    },
  },
  computed: {
    schema() {
      return yup.object({
        memberId: yup
          .string()
          .required("ID를 입력해 주세요")
          .min(8, "아이디는 최소 8자리 이상이어야 합니다.")
          .matches(
            /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
            "아이디는 영문자와 숫자를 조합하여 최소 8자리 이상이어야 합니다."
          ),
        memberPw: yup.string().required("비밀번호를 입력해 주세요"),
        rePw: yup
          .string()
          .oneOf([yup.ref("memberPw"), null], "비밀번호가 일치하지 않습니다")
          .required("비밀번호를 다시 입력해 주세요"),
        memberName: yup.string().required("이름을 입력해 주세요"),
        memberEmail: yup
          .string()
          .email("유효한 이메일을 입력해 주세요")
          .required("이메일을 입력해 주세요"),
        memberExt: yup.string().required("번호를 입력해 주세요"),
        nickname: yup.string().required("닉네임을 입력해 주세요"),
      });
    },
  },
  mounted() {
    // 회원가입 => 로그인 상태이면 회원이므로 가입 불필요
    if (this.$store.state.loggedIn == true) {
      this.$router.push("/"); // 홈으로 강제 이동
    }
  },
};
</script>
<style></style>

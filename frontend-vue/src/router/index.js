import { createRouter, createWebHistory } from "vue-router";

const routes = [
  // Home
  {
    path: "/",
    component: () => import("../views/HomeView.vue"),
  },
  // 로그인
  {
    path: "/login",
    component: () => import("../views/LoginView.vue"),
  },
  // 회원가입
  {
    path: "/register",
    component: () => import("../views/RegisterView.vue"),
  },
  // ID 찾기
  {
    path: "/find-id",
    component: () => import("../views/FindId.vue"),
  },
  // 비밀번호 찾기
  {
    path: "/find-password",
    component: () => import("../views/FindPassword.vue"),
  },
  // 회원 프로필 메인
  {
    path: "/profile",
    component: () => import("../views/profile/MyProfile.vue"),
  },
  // 비밀번호 수정
  {
    path: "/profile-edit/password",
    component: () => import("../views/profile/MyProfileEditPw.vue"),
  },
  // 닉네임 수정
  {
    path: "/profile-edit/nickname",
    component: () => import("../views/profile/MyProfileEditNick.vue"),
  },
  // 활동내역
  {
    path: "/profile-activity",
    component: () => import("../views/profile/MyActivity.vue"),
  },
  // 핫토픽 게시판
  {
    path: "/board/hot-topic",
    component: () => import("../views/board/HotTopicBoardView.vue"),
  },
  // 부서게시판
  {
    path: "/board/dept",
    component: () => import("../views/board/DeptBoardView.vue"),
  },
  // 부서게시판 상세글 조회
  {
    path: "/board/dept/:smcode/:boardId",
    component: () => import("../views/board/DeptBoardDetailView.vue"),
  },
  // 부서게시판 상세글 수정 / 삭제
  {
    path: "/board/dept-edit/:smcode/:boardId",
    component: () => import("../views/board/DeptBoardEdit.vue"),
  },
  {
    path: "/board/dept-write/:smcode",
    name: "DeptBoardWrite",
    component: () => import("../views/board/DeptBoardWrite.vue"),
  },
  
  // 동호회게시판
  {
    path: "/board/club",
    component: () => import("../views/board/ClubView.vue"),
  },
  // 동호회 - 상세글 조회
  {
    path: "/board/club/:bocode/:smcode/:boardId",
    component: () => import("../views/board/ClubDetailView.vue"),
  },
  // 동호회 - 글쓰기
  {
    path: "/board/club-write",
    component: () => import("../views/board/ClubWriteView.vue"),
  },
  // 동호회 - 글쓰기
  {
    path: "/board/club-edit/:bocode/:smcode/:boardId",
    component: () => import("../views/board/ClubEditView.vue"),
  },
  // 자유게시판
  {
    path: "/board/free",
    component: () => import("../views/board/FreeBoard.vue"),
  },
  // 자유게시판 상세글 조회
  {
    path: "/board/free/:boardId",
    component: () => import("../views/board/FreeDetailView.vue"),
  },
  // 자유게시판 등록(글쓰기)
  {
    path: "/board/free-write",
    component: () => import("../views/board/FreeWrite.vue"),
  },

    // 자유게시판 수정
    {
      path: "/board/free-edit/:boardId",
      component: () => import("../views/board/FreeBoardEdit.vue"),
    },
    // 건의게시판
    {
      path: "/board/suggest",
      component: () => import("../views/board/SuggestBoard.vue"),
    },
    // 건의게시판 상세글 조회
    {
      path: "/board/suggest/:boardId",
      component: () => import("../views/board/SuggestDetailView.vue"),
    },
    // 건의게시판 등록(글쓰기)
    {
      path: "/board/suggest-write",
      component: () => import("../views/board/SuggestWrite.vue"),
    },
    // 건의게시판 수정
    {
      path: "/board/suggest-edit/:boardId",
      component: () => import("../views/board/SuggestEdit.vue"),
    },
    // 칭찬게시판
    {
      path: "/board/praise",
      component: () => import("../views/board/PraiseBoard.vue"),
    },
    // 칭찬게시판 상세글 조회
    {
      path: "/board/praise/:boardId",
      component: () => import("../views/board/PraiseDetailView.vue"),
    },
    // 칭찬게시판 등록(글쓰기)
    {
      path: "/board/praise-write",
      component: () => import("../views/board/PraiseWrite.vue"),
    },
        // 칭찬게시판 수정
    {
      path: "/board/praise-edit/:boardId",
      component: () => import("../views/board/PraiseBoardEdit.vue"),
    },
  
  {
    path: '/board/main-search',
    name: 'SearchResults',
    component: () => import("../views/board/SearchResults.vue"),
  },
  // 관리자 게시판 관리
  {
    path: "/admin/board-mgmt",
    component: () => import("../views/admin/BoardManagement.vue"),
  },
  // 관리자 회원가입 관리
  {
    path: "/admin/register-mgmt",
    component: () => import("../views/admin/AdminRegisterMgmt.vue"),
  },
  // 관리자 회원정보 수정
  {
    path: "/admin/edit-mgmt",
    component: () => import("../views/admin/AdminEditMgmt.vue"),
  },
  // 관리자 신고 관리
  {
    path: "/admin/report-mgmt",
    component: () => import("../views/admin/AdminReportMgmt.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;

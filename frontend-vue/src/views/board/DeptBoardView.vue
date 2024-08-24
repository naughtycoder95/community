<template>
    <div class="w-100 p-3">
        <h1 class="text-center mb-5 mt-5">부서 게시판</h1>
        <!-- 게시판 소메뉴 버튼 (부서) -->
        <div class="d-flex justify-content-center mb-5">
            <button v-if="member.deptCode === 'DE01' || member.memberCode === 'AT01'" class="custom-btn col-2" @click="pageSizeChange('DE01')">
                영업팀
            </button>
            <button v-if="member.deptCode === 'DE02' || member.memberCode === 'AT01'" class="custom-btn col-2" @click="pageSizeChange('DE02')">
                인사팀
            </button>
            <button v-if="member.deptCode === 'DE03' || member.memberCode === 'AT01'" class="custom-btn col-2" @click="pageSizeChange('DE03')">
                행정팀
            </button>
            <button v-if="member.deptCode === 'DE04' || member.memberCode === 'AT01'" class="custom-btn col-2" @click="pageSizeChange('DE04')">
                보안팀
            </button>
        </div>
        <table class="table table-hover">
            <!-- 테이블 제목 -->
            <thead class="table-light text-center">
                <tr>
                    <th scope="col">글번호</th>
                    <th scope="col">제목</th>
                    <th scope="col">작성자</th>
                    <th scope="col">작성일</th>
                </tr>
            </thead>
            <!-- 테이블 : 공지사항 -->
            <tbody v-if="smcode == member?.deptCode || member.memberCode === 'AT01'">
                <tr v-for="(data, index) in deptNotice" :key="index"  @click="goBoardDetail(smcode, data.boardId)">
                    <td class="text-center col-1">{{ data.boardId }}</td>
                    <td class="col-5">
                        <span class="badge text-bg-dark me-2">공지</span>{{ data.boardTitle }} <span v-if="data.replyCount>0" style="color: brown"><i class="bi bi-chat-text"></i> [{{ data.replyCount }}]</span>
                    </td>
                    <td class="text-center col-2">{{ data.memberName }}</td>
                    <td class="text-center col-2">{{ data.addDate }}</td>
                </tr>
            </tbody>
            <!-- 테이블 : 일반글 -->
            <tbody v-if="smcode == member.deptCode || member.memberCode === 'AT01' ">
                <tr v-for="(data, index) in board" :key="index"  @click="goBoardDetail(smcode, data.boardId)"> 
                    <td class="text-center">{{ data.boardId }}</td>
                    <td class="col-5">{{ data.boardTitle }} <span v-if="data.replyCount>0" style="color: brown"><i class="bi bi-chat-text"></i> [{{ data.replyCount }}]</span></td>
                    <td class="text-center">{{ data.memberName }}</td>
                    <td class="text-center">{{ data.addDate }}</td>
                </tr>
            </tbody>
        </table>

        <!-- 페이징 -->
        <div class="row justify-content-between" v-if="smcode">
            <div class="col-4 w-25 mb-3">
                <select class="form-select form-select-sm" v-model="pageSize" @change="retrieveDept()">
                    <option v-for="(data, index) in pageSizes" :key="index" :value="data">
                        {{ data }}
                    </option>
                </select>
            </div>
            <div class="col-1">
                <button type="button" class="btn btn-dark" style="width: 90px" @click="goWritePage">글쓰기</button>
            </div>
        </div>
        <div class="row" v-if="smcode">
            <b-pagination class="col-12 mb-3 justify-content-center" v-model="page" :total-rows="count"
                :per-page="pageSize" @click="retrieveDept"></b-pagination>
            
            <!-- 검색어 입력 -->
            <div class="col-md-4 mx-auto">
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="검색어를 입력해주세요" v-model="searchTitle"
                        @keyup.enter="retrieveDept" />
                    <button class="btn btn-outline-secondary" type="button" @click="retrieveDept">
                        검색
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import DeptBoardService from "@/services/board/DeptBoardService";
export default {
    data() {
        return {
            member: this.$store.state.member,
            deptNotice: [],
            board: [],
            searchTitle: "",
            smcode: this.$store.state.member.deptCode,

            page: 1, // 현재 페이지 번호
            count: 0, // 전체 데이터 개수
            pageSize: 10, // 화면에 보여질 데이터 개수

            pageSizes: [10, 20, 30], // 화면에 보여질 개수 배열
        };
    },
    methods: {
        // 부서 게시판 - 공지글 전체 조회
        async retrieveDeptNotice() {
            try {
                let response = await DeptBoardService.getNotice(this.smcode);
                this.deptNotice = response.data;
            } catch (e) {
                console.log("retrieveDeptNotice() 에러 : ", e);
            }
        },
        // 부서 게시판 - 일반글 전체 조회
        async retrieveDept() {
            try {
                let response = await DeptBoardService.getAll(
                    this.searchTitle,
                    this.smcode,
                    this.page - 1,
                    this.pageSize
                );
                const { board, totalItems } = response.data;
                this.board = board;
                this.count = totalItems;
            } catch (e) {
                console.log("retrieveDept() 에러 : ", e);
            }
        },
        // 게시글 상세 페이지로 이동
        goBoardDetail(smcode, boardId) {
            this.$router.push(`/board/dept/${smcode}/${boardId}`);
        },
        // 페이징 : select 태그에 바인딩
        pageSizeChange(dept) {
            this.page = 1;          // 현재패이지번호 : 1
            this.smcode = dept;
            this.searchTitle = "";
            this.retrieveDept();    // 재조회
            this.retrieveDeptNotice();
        },
        // 글쓰기 페이지로 이동
        goWritePage() {
            this.$router.push(`/board/dept-write/${this.smcode}`);
        },
    },
    mounted() {
        this.retrieveDept();
        this.retrieveDeptNotice();
        console.log("회원", this.member);
    },
};
</script>

<style scoped>
.custom-btn {
  background-color: transparent;
  border: none;
  color: #333;
  font-size: 16px;
  padding: 15px 20px;
  cursor: pointer;
  position: relative;
}

.custom-btn::after { /* 모든 버튼에 기본 밑줄 추가 */
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 1px;
  background-color: lightgray; /* 밝은 회색 밑줄 */
  transition: background-color 0.3s ease; /* 색상 변화 애니메이션 */
}

.custom-btn.active,
.custom-btn:hover {
  color: #000;
}

.custom-btn.active::after, /* 활성/호버 시 밑줄 색상 변경 */
.custom-btn:hover::after {
  background-color: #000; /* 검정색 밑줄 */
}
</style>

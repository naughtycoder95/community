<template>
    <div class="container mt-3 board-detail-container">
        <!-- 글 수정/삭제 버튼 -->
        <div class="row mb-3 board-button d-flex justify-content-end me-2">
            <button v-if="auth === 'B'" class="col-1 btn btn-warning me-2" @click="moveToEdit">수정</button>
            <button v-if="auth === 'A' || auth === 'B'" class="col-1 btn btn-danger"
                @click="confirmBoDelete">삭제</button>
        </div>
        <!-- 게시글 -->
        <div class="card mb-3">
            <div class="card-header text-white">
                부서게시판 <i class="bi bi-caret-right"></i> {{ cmcd?.cmCdName }}
            </div>
            <div class="card-body">
                <h5 class="card-title">{{ board?.boardTitle }}</h5>
                <p class="card-text-name">{{ board?.memberName }}</p>
                <p class="card-text-date">{{ board?.addDate }}</p>
                <hr>
                <!-- 글 내용 -->
                <p class="card-text-content">{{ board?.boardContent }}</p>
                <!-- 투표 -->
                <div class="card mb-3" style="width: 25rem" v-if="vote">
                    <h5 class="card-header vote-card-header">
                        {{ vote[0]?.voteName }}
                    </h5>
                    <div class="card-body">
                        <div v-for="(data, index) in vote" :key="index"
                            :class="{ 'highlight': data.voteId === voteMember.voteId }">
                            <div class="form-check-vote d-flex justify-content-between align-items-center">
                                <input v-if="!voteMember" class="form-check-input input-vote" type="radio"
                                    name="voteRadios" id="voteRadios" :value="data.voteId" />
                                <label class="form-check-label vote-label" for="voteRadios">
                                    {{ data.voteList }}
                                </label>
                                <label class="form-check-label vote-count" for="voteRadios">
                                    {{ data.voteCnt }}
                                </label>
                            </div>
                        </div>
                        <p v-if="vote.length && vote[0].status === 'Y'" class="card-text-date mt-2">종료 날짜: {{
                            vote[0]?.delDate }}</p>
                        <p v-if="vote.length && vote[0].status === 'N'" class="card-text-date mt-2">종료된 투표입니다.</p>
                        <div class="d-md-flex justify-content-md-end">
                            <button v-if="vote.length && vote[0].status === 'N'" type="button" class="btn btn-secondary"
                                disabled>투표하기</button>
                            <button v-else-if="!voteMember" type="button" class="btn btn-secondary"
                                @click="saveVote()">투표하기</button>
                        </div>
                    </div>
                </div>
                <!-- 장소 -->
                <div v-if="address">
                    <div id="map" style="width: 600px; height: 400px" ref="map" class="img-thumbnail"></div>
                </div>
                <!-- 이미지 -->
                <div class="board-images mb-3">
                    <div v-for="(data, index) in images" :key="index" class="mb-2">
                        <img :src="data.fileUrl" class="img-fluid">
                    </div>
                </div>
                <!-- 파일 -->
                <div class="board-images mb-3">
                    <div v-for="(data, index) in nonImages" :key="index" class="mb-2">
                        <a :href="data.fileUrl" class="btn btn-outline-dark btn-sm btn-outline-grey" download> <i
                                class="bi bi-download"></i> {{ data.fileName }}</a>
                    </div>
                </div>
                <hr>
                <!-- 글 하단 아이콘 -->
                <div class="d-flex justify-content-start">
                    <!-- 추천 아이콘 -->
                    <div v-if="recommendIcon" @click="toggleRecommend" class="me-3" type="button"><i
                            class="bi bi-hand-thumbs-up"></i> {{ recommendCnt }} </div>
                    <div v-else class="me-3" type="button"><i class="bi bi-hand-thumbs-up-fill"></i> {{ recommendCnt }}
                    </div>
                    <!-- 댓글 아이콘 -->
                    <div class="me-3" type="button"><i class="bi bi-chat-text"></i> {{ replyCount }} </div>
                    <!-- 신고 아이콘 -->
                    <div v-if="auth === 'C'" class="me-3" type="button" data-bs-toggle="modal"
                        data-bs-target="#reportModal"><i class="bi bi-exclamation-triangle"></i>신고</div>
                </div>
            </div>
        </div>
        <!-- 글 신고 Modal -->
        <div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="reportModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form class="was-validated">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="reportModalLabel">게시글 신고</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-2 row">
                                <label for="staticWriter" class="col-sm-2 col-form-label">작성자</label>
                                <div class="col-sm-10">
                                    <input type="text" readonly class="form-control-plaintext" id="staticWriter"
                                        v-model="board.memberName">
                                </div>
                            </div>
                            <div class="mb-2 row">
                                <label for="staticTitle" class="col-sm-2 col-form-label">내용</label>
                                <div class="col-sm-10">
                                    <input type="text" readonly class="form-control-plaintext" id="staticTitle"
                                        v-model="board.boardTitle">
                                </div>
                            </div>
                            <div class="mb-2">
                                <hr />
                                <textarea rows="10" class="form-control" placeholder="신고 사유를 입력하세요." required
                                    v-model="reportReason"></textarea>
                                <div class="invalid-feedback">
                                    신고 사유는 필수 입력값입니다.
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                            <button type="submit" class="btn btn-danger" @click="createReport">신고</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- 댓글 목록 -->
        <div class="reply-content mb-3">
            <ul v-for="(data, index) in reply" :key="index" class="list-group mb-3">
                <!-- 댓글 -->
                <li v-if="!data.isEditing" class="list-group-item">
                    <div class="reply-name">{{ data.memberName }}</div>
                    <div class="reply-content">{{ data.reply }}</div>
                    <img v-if="data.fileUrl" :src="data.fileUrl" class="img-fluid img-thumbnail w-25" alt="이미지" />
                    <div class="d-flex justify-content-between mt-2">
                        <div class="reply-date">{{ data.addDate }}</div>
                        <div>
                            <button v-if="this.member.memberId === data.memberId" class="btn btn-secondary me-3"
                                @click="openReplyUpdate(data.replyId)">수정</button>
                            <button v-if="this.member.memberId === data.memberId || auth === 'A'"
                                class="btn btn-danger me-3" @click="deleteReply(data)">삭제</button>
                            <button v-if="this.member.memberId !== data.memberId && auth !== 'A'"
                                class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#reportReplyModal"
                                @click="openReplyReport(data)">
                                <i class="bi bi-exclamation-triangle"></i> 신고
                            </button>
                        </div>
                    </div>
                    <button class="btn btn-secondary mt-2" @click="openReReply(data.replyId)">대댓글쓰기</button>

                    <!-- 대댓글 작성 폼 (대댓글쓰기 버튼 클릭 시 나타남) -->
                    <div v-if="showWriteReReply && parentId === data.replyId" class="card mb-3 mt-3">
                        <div class="card-body">
                            <div class="reply-name">{{ memberInfo.memberName }} (익명게시판은 별명으로 변경하세요)</div>

                            <textarea v-model.lazy="reReplyTextarea" placeholder="대댓글을 남겨보세요"
                                class="form-control mb-2 reply-content"></textarea>
                            <div class="d-flex justify-content-between">
                                <!-- 파일첨부 -->
                                <input class="form-control file-upload-input w-auto" type="file"
                                    @change="selectReReplyFile($event)" />
                                <button class="btn btn-primary file-upload-button"
                                    @click="createReReply(data.replyId)">등록</button>
                            </div>
                        </div>
                    </div>
                </li>
                <!-- 댓글 수정 버튼 클릭 시 보일 부분 -->
                <li v-if="data.isEditing" class="list-group-item">
                    <div class="reply-name">{{ data.memberName }} </div>
                    <textarea v-model.lazy="data.reply" placeholder="댓글을 남겨보세요"
                        class="form-control mb-2 reply-content"></textarea>
                    <div class="d-flex">
                        <!-- 파일첨부 -->
                        <input class="form-control file-upload-input w-auto mb-2" type="file"
                            @change="selectReplyFile2($event, data)" />
                        <div v-if="data.fileUrl">{{ data.fileName }} <button @click="removeFile(data)"
                                class="btn btn-sm text-danger ms-2">x</button> </div>
                    </div>
                    <div class="d-flex justify-content-between">
                        <button class="btn btn-secondary" @click="closeReplyUpdate(data.replyId)">취소</button>
                        <button class="btn btn-secondary" @click="updateReply(data.replyId)">등록</button>
                    </div>
                </li>
                <!-- 대댓글 -->
                <div v-for="(reReply, index) in data.reReplies" :key="index">
                    <li v-if="!reReply.isReReplyEditing" class="list-group-item reReply-container">
                        <div class="reply-name"><i class="bi bi-arrow-return-right"></i> {{ reReply.memberName }} </div>
                        <div class="reply-content">{{ reReply.reply }}</div>
                        <img v-if="reReply.fileUrl" :src="reReply.fileUrl" class="img-fluid img-thumbnail w-25"
                            alt="이미지" />
                        <div class="d-flex justify-content-between mt-2">
                            <div class="reply-date">{{ reReply.addDate }}</div>
                            <div>
                                <button v-if="this.member.memberId === reReply.memberId" class="btn btn-secondary me-2"
                                    @click="openReReplyUpdate(reReply.replyId)">수정</button>
                                <button v-if="this.member.memberId === reReply.memberId || auth === 'A'"
                                    class="btn btn-danger me-2" @click="deleteReply(reReply)">삭제</button>
                                <button v-if="this.member.memberId !== reReply.memberId && auth !== 'A'"
                                    class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#reportReplyModal"
                                    @click="openReplyReport(reReply)">
                                    <i class="bi bi-exclamation-triangle"></i> 신고
                                </button>
                            </div>
                        </div>
                    </li>
                    <!-- 대댓글 수정 버튼 클릭 시 보일 부분 -->
                    <li v-if="reReply.isReReplyEditing" class="list-group-item">
                        <div class="reply-name"><i class="bi bi-arrow-return-right"></i> {{ reReply.memberName }} </div>
                        <textarea v-model.lazy="reReply.reply" placeholder="댓글을 남겨보세요"
                            class="form-control mb-2 reply-content"></textarea>
                        <div class="d-flex">
                            <!-- 파일첨부 -->
                            <input class="form-control file-upload-input w-auto mb-2" type="file" ref="replyFile"
                                @change="selectReReplyFile2($event, reReply)" />
                            <div v-if="reReply.fileUrl">{{ reReply.fileName }} <button @click="removeFile(reReply)"
                                    class="btn btn-sm text-danger ms-2">x</button> </div>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button class="btn btn-secondary" @click="closeReReplyUpdate(reReply.replyId)">취소</button>
                            <button class="btn btn-secondary" @click="updateReReply(reReply)">등록</button>
                        </div>
                    </li>
                </div>
            </ul>
        </div>
        <!-- 페이징 -->
        <b-pagination v-if="reply" class="col-12 mb-3 justify-content-center" v-model="replyPage"
            :total-rows="replyPageCount" :per-page="pageSize" @click="retrieveReply()"></b-pagination>
        <!-- 댓글 신고 Modal -->
        <div class="modal fade" id="reportReplyModal" tabindex="-1" aria-labelledby="reportReplyModalLabel"
            aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form class="was-validated">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="reportReplyModalLabel">댓글 신고</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-2 row">
                                <label for="staticWriter" class="col-sm-2 col-form-label">작성자</label>
                                <div class="col-sm-10">
                                    <input type="text" readonly class="form-control-plaintext" id="staticWriter"
                                        v-model="report.memberName">
                                </div>
                            </div>
                            <div class="mb-2 row">
                                <label for="staticTitle" class="col-sm-2 col-form-label">내용</label>
                                <div class="col-sm-10">
                                    <input type="text" readonly class="form-control-plaintext" id="staticTitle"
                                        v-model="report.reply">
                                </div>
                            </div>
                            <div class="mb-2">
                                <hr />
                                <textarea rows="10" class="form-control" placeholder="신고 사유를 입력하세요." required
                                    v-model="report.reportReason"></textarea>
                                <div class="invalid-feedback">
                                    신고 사유는 필수 입력값입니다.
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                            <button type="submit" class="btn btn-danger" @click="createReplyReport">신고</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- 댓글 작성 -->
        <div class="card mb-3">
            <div class="card-body">
                <div class="reply-name">{{ memberInfo.memberName }}</div>
                <textarea v-model.lazy="replyTextarea" placeholder="댓글을 남겨보세요"
                    class="form-control mb-2 reply-content"></textarea>
                <div class="d-flex justify-content-between">
                    <!-- 파일첨부 -->
                    <input class="form-control file-upload-input w-auto" type="file" ref="replyFile"
                        @change="selectReplyFile" />
                    <button class="btn btn-primary file-upload-button" @click="createReply">등록</button>
                </div>
            </div>
        </div>
        <!-- 목록으로 돌아가기 버튼 -->
        <div class="d-grid">
            <button class="col-1 btn btn-secondary" @click="this.$router.push(`/board/dept`)">목록</button>
        </div>
    </div>
</template>


<script>
import BoardDetailService from '@/services/board/BoardDetailService';
import ReplyService from '@/services/board/ReplyService';

export default {
    data() {
        return {
            member: this.$store.state.member,       // 현재 로그인된 회원 가져오기
            boardId: this.$route.params.boardId,    // 현재 글 ID 가져오기
            smcode: this.$route.params.smcode,      // 현재 소메뉴 코드 가져오기

            auth: "",                     // 로그인 사용자 권한 체크
            replyTextarea: "",
            reReplyTextarea: "",
            parentId: "",                 // 현재 대댓글의 상위 댓글의 replyId
            recommendIcon: true,          // 추천 아이콘 (true는 빈 아이콘)
            reportReason: "",             // 글 신고 사유
            report: {                     // 댓글 신고 객체
                replyId: "",
                memberName: "",           // 댓글 작성자명
                reply: "",                // 댓글 내용
                reportReason: ""          // 신고 사유
            },
            currentFile: undefined,       // 댓글파일선택
            currentReFile: undefined,     // 대댓글파일선택
            isFileDeleted: false,         // 댓글파일 삭제여부
            showWriteReReply: false,      // 대댓글쓰기
            images: [],
            nonImages: [],
            specificVoteId: "",

            // 페이징
            pageSize: 5,        // 화면에 보여질 개수
            replyPage: 1,       // 현재 페이지 번호
            replyPageCount: 0,  // 댓글 전체 데이터 개수

            // retrieve 
            memberInfo: "",     // 회원정보
            board: "",          // 게시글
            cmcd: "",           // 부서코드, 부서명
            vote: "",           // 투표
            voteMember: [],     // 투표 회원
            boardFile: [],      // 글 첨부 이미지
            existsReport: "",   // 글 신고 존재 여부
            existsReplyReport: 0,   // 댓글 신고 존재 여부
            recommend: "",      // 추천 존재 여부
            recommendCnt: "",   // 추천 수
            reply: "",          // 댓글 목록
            replyCount: "",     // 댓글수
            currentUrl: window.location.pathname,   // 현재 페이지 Url

            // 장소 
            map: null,
            infowindow: null,
            markers: [],
            options: {
                //지도를 생성할 때 필요한 기본 옵션
                center: {
                    lat: 33.450701,
                    lng: 126.570667,
                }, //지도의 중심좌표.
                level: 4, //지도의 레벨(확대, 축소 정도)
            },
            address: "",
        };
    },
    methods: {
        // 회원 권한 체크
        async checkAuth() {
            if (this.member.memberCode === "AT01") {
                // 관리자 로그인
                this.auth = "A";
            } else if (this.member.memberCode === "AT02" && this.member.memberId === this.board.memberId) {
                // 글 작성자일경우
                this.auth = "B";
            } else {
                // 기타 회원
                this.auth = "C";
            }
        },
        // 게시글 삭제
        async deleteBoard() {
            try {
                let response = await BoardDetailService.deleteBoard(this.boardId);
                console.log("삭제", response);
                this.$router.push(`/board/dept`);
                alert("삭제되었습니다.");
            } catch (error) {
                console.log("삭제 에러", error);
                alert("삭제에 실패했습니다.");
            }
        },
        // 게시글 삭제 확인
        confirmBoDelete() {
            if (confirm("게시글을 삭제 하시겠습니까?")) {
                this.deleteBoard();
            }
        },
        // 댓글 삭제 확인
        removeCheck() {
            if (confirm("댓글을 삭제하시겠습니까?") == false) {
                document.form.submit();
            } else {
                return true;
            }
        },
        // 글 수정 페이지로 이동
        moveToEdit() {
            this.$router.push(`/board/dept-edit/${this.smcode}/${this.boardId}`);
        },
        // 추천 버튼 클릭 시 호출 
        toggleRecommend() {
            this.recommendIcon = !this.recommendIcon;
            if (this.recommendIcon == false) {
                this.saveRecommend().then(() => {
                    // 추천 저장 후, 추천 수 다시 불러오기
                    this.retrieveRecommendCnt();
                }).catch(e => {
                    console.error("추천 저장 실패:", e);
                });
            }
        },
        // 추천 저장함수
        async saveRecommend() {
            try {
                let recommend = {
                    boardId: this.boardId,
                    memberId: this.member.memberId
                };
                await BoardDetailService.createRecommend(recommend);
                console.log("추천 저장 완료")
            } catch (e) {
                console.log("saveRecommend 에러", e);
            }
        },
        // 글 신고 저장
        async createReport() {
            try {
                if (this.existsReport === 1) {
                    alert("이미 신고한 글입니다.");
                } else {
                    if (!this.reportReason) {
                        alert("신고 사유를 입력해주세요.");
                    } else {
                        let report = {
                            memberId: this.member.memberId,
                            boardId: this.boardId,
                            reportReason: this.reportReason,
                        }
                        await BoardDetailService.createReport(report);
                        alert("신고가 완료되었습니다.");
                        this.reportReason = "";
                    }
                }
            } catch (e) {
                console.log("createReport 에러", e);
            }
        },
        // 댓글 신고 저장
        async createReplyReport() {
            try {
                if (this.existsReplyReport === 1) {
                    alert("이미 신고한 댓글입니다.");
                } else {
                    if (!this.report.reportReason) {
                        alert("신고 사유를 입력해주세요.");
                    } else {
                        let report = {
                            memberId: this.member.memberId,
                            replyId: this.report.replyId,
                            reportReason: this.report.reportReason,
                        }
                        await ReplyService.createReplyReport(report);
                        alert("신고가 완료되었습니다.");
                        this.report.reportReason = "";
                    }
                }

            } catch (e) {
                console.log("createReplyReport 에러", e);
            }
        },
        // 댓글 신고 모달 열기
        openReplyReport(data) {
            // 반복문의 현재 댓글 정보(댓글Id, 작성자명, 댓글내용) 저장
            this.report.replyId = data.replyId;
            this.report.memberName = data.memberName;
            this.report.reply = data.reply;
            this.retrieveReplyReport(data);
        },
        // 파일 타입 분류
        classifyFilesByType() {
            if (this.boardFile) {
                this.boardFile.forEach(file => {
                    if (file.fileName.substring(file.fileName.lastIndexOf('.') + 1) === "png" ||
                        file.fileName.substring(file.fileName.lastIndexOf('.') + 1) === "PNG" ||
                        file.fileName.substring(file.fileName.lastIndexOf('.') + 1) === "jpg" ||
                        file.fileName.substring(file.fileName.lastIndexOf('.') + 1) === "jpeg" ||
                        file.fileName.substring(file.fileName.lastIndexOf('.') + 1) === "gif") {
                        this.images.push(file);
                    } else {
                        this.nonImages.push(file);
                    }
                });
            }
        },
        // 투표 저장
        async saveVote() {
            try {
                let vote = {
                    memberId: this.member.memberId,
                    boardId: this.boardId,
                    voteId: document.querySelector('input[name="voteRadios"]:checked').value        // 현재 선택된 투표 ID
                }
                await BoardDetailService.createVoteMember(vote);
                this.retrieveVote();
                this.retrieveVoteMember();
            } catch (e) {
                console.log();
            }
        },

        // ------------------------ retrieve 함수 ------------------------
        // 로그인된 회원 정보 가져오기
        async retrieveMember() {
            try {
                let response = await BoardDetailService.getMember(this.member.memberId);
                this.memberInfo = response.data;
                this.checkAuth();
            } catch (e) {
                console.log("retrieveMember 에러", e);
            }
        },
        // 게시글, 작성자 정보 가져오기
        async retrieveBoard() {
            try {
                let response = await BoardDetailService.getBoard(this.boardId);
                this.board = response.data;
            } catch (e) {
                console.log("retrieveBoard 에러", e);
            }
        },
        // 코드번호로 코드명 가져오기
        async retrieveCode() {
            try {
                let response = await BoardDetailService.getCmCd(this.smcode);
                this.cmcd = response.data;
            } catch (e) {
                console.log("retrieveCode 에러", e);
            }
        },
        // 글번호로 투표 가져오기
        async retrieveVote() {
            try {
                let response = await BoardDetailService.getVote(this.boardId);
                this.vote = response.data;
            } catch (e) {
                console.log("retrieveVote 에러", e);
            }
        },
        // 투표 회원 가져오기
        async retrieveVoteMember() {
            try {
                let response = await BoardDetailService.getVoteMember(this.boardId, this.member.memberId);
                this.voteMember = response.data;
            } catch (e) {
                console.log("retrieveVoteMember 에러", e);
            }
        },
        // 글번호로 장소 가져오기
        async retrievePlace() {
            try {
                let response = await BoardDetailService.getPlace(this.boardId);
                this.address = response.data.address;
                if (!this.address) return;
                let kakao = window.kakao;
                var container = this.$refs.map;
                const { center, level } = this.options;

                let map = new kakao.maps.Map(container, {
                    center: new kakao.maps.LatLng(center.lat, center.lng),
                    level,
                }); //지도 생성 및 객체 리턴
                this.map = map;

                // 주소-좌표 변환 객체를 생성합니다
                var geocoder = new kakao.maps.services.Geocoder();

                // 주소로 좌표를 검색합니다
                geocoder.addressSearch(this.address, function (result, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === kakao.maps.services.Status.OK) {
                        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                        // 결과값으로 받은 위치를 마커로 표시합니다
                        var marker = new kakao.maps.Marker({
                            map: map,
                            position: coords,
                        });

                        // 인포윈도우로 장소에 대한 설명을 표시합니다
                        var infowindow = new kakao.maps.InfoWindow({
                            content:
                                `<div style="width:150px;text-align:center;padding:6px 0;">${this.address}</div>`,
                        });
                        infowindow.open(map, marker);

                        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                        map.setCenter(coords);
                    }
                }.bind(this));
            } catch (e) {
                console.log("장소를 표시할 수 없습니다.", e);
            }
        },
        // 글번호로 이미지 가져오기
        async retrieveImg() {
            try {
                let response = await BoardDetailService.getImg(this.boardId);
                this.boardFile = response.data;
            } catch (e) {
                console.log("retrieveImg 에러", e);
            }
        },
        // 신고 데이터 존재 여부 가져오기
        async retrieveReport() {
            try {
                let response = await BoardDetailService.getReport(this.boardId, this.member.memberId);
                this.existsReport = response.data;
            } catch (e) {
                console.log("retrieveReport 에러", e);
            }
        },
        // 댓글 신고 데이터 존재 여부 가져오기
        async retrieveReplyReport(data) {
            try {
                let response = await BoardDetailService.getReplyReport(data.replyId, this.member.memberId);
                this.existsReplyReport = response.data;
            } catch (e) {
                console.log("retrieveReport 에러", e);
            }
        },
        // 추천 데이터 존재 여부 가져오기
        async retrieveRecommend() {
            try {
                let response = await BoardDetailService.getRecommend(this.boardId, this.member.memberId);
                this.recommend = response.data;
                if (response.data === 1) {
                    this.recommendIcon = false;
                } else {
                    this.recommendIcon = true;
                }
            } catch (e) {
                console.log("retrieveRecommend 에러", e);
            }
        },
        // 추천 수 가져오기
        async retrieveRecommendCnt() {
            try {
                let response = await BoardDetailService.getRecommendCnt(this.boardId);
                this.recommendCnt = response.data;
            } catch (e) {
                console.log("retrieveRecommendCnt 에러", e)
            }
        },
        // 글번호로 댓글 가져오기
        async retrieveReply() {
            try {
                let response = await ReplyService.getReply(this.boardId, this.replyPage - 1, this.pageSize);
                this.reply = response.data.content;
                this.replyPageCount = response.data.totalElements;

                // 각 댓글에 대한 대댓글 가져오기
                const reReplyPromise = this.reply.map(async (comment) => {
                    let reReplyResponse = await ReplyService.getReReply(this.boardId, comment.replyId);
                    comment.reReplies = reReplyResponse.data;
                    return comment;
                });

                // 모든 대댓글 요청이 완료될 때까지 기다리기
                this.reply = await Promise.all(reReplyPromise);
            } catch (e) {
                console.log("댓글 없음", e);
            }
        },
        // 댓글 수 가져오기
        async retrieveReplyCount() {
            try {
                let response = await ReplyService.getReplyCount(this.boardId);
                this.replyCount = response.data;
            } catch (e) {
                console.log("retrieveReplyCount 에러", e);
            }
        },

        // ------------------------ 댓글 CUD 관련 함수 ------------------------
        // 새 댓글 작성 시 파일 선택
        selectReplyFile() {
            this.currentFile = this.$refs.replyFile.files[0];
        },
        // 댓글 수정 시 파일 선택상자
        selectReplyFile2(event, data) {
            data.fileName = null;
            data.fileUrl = null;
            this.currentFile = event.target.files[0];
        },
        // 댓글(대댓글) 수정시 파일 삭제 버튼
        removeFile(data) {
            data.fileName = null;
            data.fileUrl = null;
            this.currentFile = null;
            this.currentReFile = null;
            this.isFileDeleted = true;
        },
        // 댓글 + 파일 저장
        async createReply() {
            try {
                let temp = {
                    boardId: this.boardId,
                    memberId: this.member.memberId,
                    reply: this.replyTextarea,
                    reReply: "",
                }
                if (temp.reply) {
                    let response = await ReplyService.createReply(temp, this.currentFile, this.currentUrl);
                    console.log("댓글 전송 : ", response);
                    this.retrieveReply();
                    this.retrieveReplyCount();
                    this.replyTextarea = "";
                    this.currentFile = undefined;
                } else {
                    alert("댓글을 입력하세요.");
                }
            } catch (e) {
                this.currentFile = undefined;
                console.log(e);
            }
        },
        // 댓글 수정 버튼 클릭 시 호출
        openReplyUpdate(replyId) {
            const reply = this.reply.find(r => r.replyId === replyId);
            if (reply) {
                reply.isEditing = true;
            }
        },
        closeReplyUpdate(replyId) {
            const reply = this.reply.find(r => r.replyId === replyId);
            if (reply) {
                reply.isEditing = false;
            }
            this.retrieveReply();
        },
        // 댓글 수정 후 등록
        async updateReply(replyId) {
            const reply = this.reply.find(r => r.replyId === replyId);
            reply.reReply = "";    // reReply에는 빈문자열 전달 (undefined 에러 방지)
            reply.isFileDeleted = this.isFileDeleted;
            try {
                if (reply.reply) {
                    let response = await ReplyService.updateReply(reply, this.currentFile);
                    console.log("댓글 수정 : ", response.data);
                    this.retrieveReply();
                    this.retrieveReplyCount();
                    this.isFileDeleted = false;
                } else {
                    alert("댓글을 입력하세요.");
                }
            } catch (e) {
                console.log("updateReply 에러", e);
            }
        },
        // 댓글(대댓글) 삭제
        async deleteReply(data) {
            try {
                if (data.reReply === null) {    // 댓글일 때
                    if (data.reReplies) {   // 댓글의 대댓글이 있을 경우
                        alert("대댓글이 있는 댓글은 삭제할 수 없습니다.");
                        return;
                    } else {    // 댓글의 대댓글이 없을 경우
                        this.removeCheck();
                    }
                } else {    // 대댓글일 때
                    this.removeCheck();
                }
                await ReplyService.deleteReply(data.replyId);
                this.retrieveReply();
                this.retrieveReplyCount();
                console.log("댓글 삭제 성공");
            } catch (e) {
                console.log("deleteReply 에러 : ", e);
            }
        },

        // ------------------------ 대댓글 CUD 관련 함수 ------------------------
        // 대댓글 쓰기 버튼 클릭 시 호출
        openReReply(replyId) {
            this.parentId = replyId;     // 현재 댓글ID를 저장
            this.showWriteReReply = !this.showWriteReReply;     // 대댓글쓰기창 토글
        },
        // 새 대댓글 작성 시 파일 선택
        selectReReplyFile(event) {
            this.currentReFile = event.target.files[0];
        },
        // 대댓글 수정 시 파일 선택
        selectReReplyFile2(event, data) {
            data.fileName = null;
            data.fileUrl = null;
            this.currentReFile = event.target.files[0];
        },
        // 대댓글 + 파일 저장
        async createReReply(reReplyData) {
            try {
                let temp = {
                    boardId: this.boardId,
                    memberId: this.member.memberId,
                    reply: this.reReplyTextarea,
                    reReply: reReplyData,
                }
                if (temp.reply) {
                    let response = await ReplyService.createReply(temp, this.currentReFile, this.currentUrl);
                    console.log("대댓글 전송 : ", response);
                    this.retrieveReply();
                    this.retrieveReplyCount();
                    this.reReplyTextarea = "";
                    this.currentReFile = undefined;
                    this.showWriteReReply = !this.showWriteReReply;     // 대댓글쓰기창 토글
                } else {
                    alert("댓글을 입력하세요.");
                }
            } catch (e) {
                this.currentReFile = undefined;
                this.showWriteReReply = !this.showWriteReReply;     // 대댓글쓰기창 토글
                console.log(e);
            }
        },
        // 대댓글 수정 버튼 클릭 시 호출
        openReReplyUpdate(replyId) {
            this.reply.forEach(reply => {
                const reReplies = Array.isArray(reply.reReplies) ? reply.reReplies : [];
                const reReply = reReplies.find(r => r.replyId === replyId);
                if (reReply) {
                    reReply.isReReplyEditing = true;
                }
            });
        },
        closeReReplyUpdate(replyId) {
            this.reply.forEach(reply => {
                const reReplies = Array.isArray(reply.reReplies) ? reply.reReplies : [];
                const reReply = reReplies.find(r => r.replyId === replyId);
                if (reReply) {
                    reReply.isReReplyEditing = false;
                }
            });
            this.retrieveReply();
        },
        // 대댓글 수정 후 등록
        async updateReReply(reReply) {
            reReply.isFileDeleted = this.isFileDeleted;
            try {
                if (reReply.reply) {
                    let response = await ReplyService.updateReply(reReply, this.currentReFile);
                    console.log("대댓글 수정 : ", response.data);
                    this.retrieveReply();
                    this.retrieveReplyCount();
                    this.isFileDeleted = false;
                } else {
                    alert("댓글을 입력하세요.");
                }
            } catch (e) {
                console.log("updateReReply 에러", e);
            }
        },
    },
    async mounted() {
        await this.retrieveMember();
        await this.retrieveBoard();
        await this.retrieveReply();
        await this.retrieveImg();
        this.retrieveReport();
        this.classifyFilesByType();
        this.checkAuth();
        this.retrieveCode();
        this.retrieveVote();
        this.retrieveVoteMember();
        this.retrievePlace();
        this.retrieveRecommend();
        this.retrieveRecommendCnt();
        this.retrieveReplyCount();

        let placeResponse = await BoardDetailService.getPlace(this.boardId);
        if (placeResponse.data.address) {
            this.address = placeResponse.data.address;
            if (window.kakao && window.kakao.maps) {
                this.retrievePlace();
            } else {
                const script = document.createElement("script");
                script.onload = () => {
                    this.retrievePlace();
                };
                script.src =
                    "//dapi.kakao.com/v2/maps/sdk.js?appkey=55b411073309a73c48d56caa594311c8"; // 발급받은 API 키로 변경
                document.head.appendChild(script);
            }
        }
    },
}
</script>

<style scoped>
@import "@/assets/css/boardDetail.css";
</style>

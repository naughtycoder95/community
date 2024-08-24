<template>
  <div align="center">
    <h1 class="text-center mb-5 mt-5">자유 게시판</h1>
  </div>
  <div class="container free-box">
    <!-- 자유게시판 / 닉네임 -->
    <div align="center">
      <div class="row">
        <div class="form-group col-4">
          <input
            type="text"
            class="form-control"
            placeholder="자유 게시판"
            disabled
          />
        </div>
      </div>

      <!-- 제목 / 공지사항 -->
      <div class="row mt-3">
        <div class="col-md-10">
          <input
            type="text"
            class="form-control"
            placeholder="제목을 입력해주세요"
            name="boardTitle"
            v-model="board.boardTitle"
          />
        </div>
        <!-- 관리자만 보이게 해야함 -->
        <div
          v-if="isAdmin"
          class="col-md-2 d-flex align-items-center justify-content-end"
        >
          <div class="form-check">
            <input
              class="form-check-input"
              type="checkbox"
              v-model="board.noticeYn"
            />
            <label class="form-check-label" for="noticeCheck">공지사항</label>
          </div>
        </div>
      </div>

      <!-- 투표 추가 / 장소 추가 -->
      <div class="d-grid gap-3 d-md-block mt-3 mb-3 text-start">
        <button
          type="button"
          class="btn btn-outline-dark me-3"
          data-bs-toggle="modal"
          data-bs-target="#vote-modal"
        >
          <i class="bi bi-bar-chart-line"></i> 투표추가
        </button>
        <!-- 투표추가 모달 -->
        <div
          class="modal fade"
          id="vote-modal"
          tabindex="-1"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">
                  투표 등록
                </h1>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <h5 class="text-start">투표명</h5>
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="제목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteName"
                />
                <hr />
                <h5 class="text-start">항목 추가</h5>
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="1. 항목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteList.vote1"
                />
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="2. 항목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteList.vote2"
                />
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="3. 항목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteList.vote3"
                />
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="4. 항목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteList.vote4"
                />
                <input
                  type="text"
                  class="form-control mb-3"
                  placeholder="5. 항목을 입력하세요"
                  name="boardTitle"
                  v-model.lazy="vote.voteList.vote5"
                />
                <hr />
                <h5 class="text-start">
                  종료일 :
                  <input type="date" v-model="vote.delDate" :min="minDate" />
                </h5>
              </div>
              <div class="modal-footer">
                <button
                  @reset="vote"
                  class="btn btn-secondary"
                  data-bs-dismiss="modal"
                  @click="resetVoteForm"
                >
                  취소
                </button>
                <button
                  type="button"
                  class="btn btn-primary"
                  data-bs-dismiss="modal"
                  @click="submitVote"
                >
                  등록
                </button>
              </div>
            </div>
          </div>
          <!-- 투표추가 모달 끝 -->
        </div>
        <button
          type="button"
          class="btn btn-outline-dark me-3"
          data-bs-toggle="modal"
          data-bs-target="#place-modal"
        >
          <i class="bi bi-geo-alt"></i> 장소추가
        </button>
        <!-- 장소 Modal -->
        <div
          class="modal fade"
          id="place-modal"
          tabindex="-1"
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <div class="row">
                  <div class="col-auto">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">
                      장소 추가
                    </h1>
                  </div>
                  <div class="col-auto">
                    <input
                      type="button"
                      @click="openPostcode"
                      value="주소 검색"
                      class="btn btn-dark"
                    />
                  </div>
                </div>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <div
                  id="map"
                  ref="mapContainer"
                  style="
                    width: 100%;
                    height: 500px;
                    margin-top: 10px;
                    display: none;
                  "
                ></div>
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  @click="resetPlace"
                  data-bs-dismiss="modal"
                >
                  취소
                </button>
                <button
                  type="button"
                  class="btn btn-primary"
                  data-bs-dismiss="modal"
                >
                  확인
                </button>
              </div>
            </div>
          </div>
        </div>
        <!-- 장소 Modal 끝 -->
        <span v-if="address"
          ><i class="bi bi-geo-alt-fill"></i> {{ address }}</span
        >
      </div>
    </div>

    <!-- 안내 메시지 -->
    <div class="row">
      <div class="col-md-12">
        <div class="alert alert-info" role="alert" v-if="showSuccessMessage">
          투표가 등록되었습니다.
        </div>
      </div>
    </div>

    <!-- 내용 입력 -->
    <div class="row">
      <div class="col-md-12">
        <textarea
          class="form-control"
          placeholder="내용을 입력해주세요"
          rows="10"
          name="boardContent"
          v-model="board.boardContent"
        ></textarea>
      </div>
    </div>

    <!-- 파일 첨부 -->
    <div class="row mt-3">
      <div class="col-md-12">
        <input
          type="file"
          class="form-control"
          multiple
          @change="handleFileUpload"
          ref="fileInput"
        />
        <div class="mt-2 file-list">
          <div v-for="(file, index) in files" :key="index" class="file-item">
            <p class="d-inline">{{ file.name }}</p>
            <button
              type="button"
              class="btn btn-danger btn-sm ms-2"
              @click="removeFile(index)"
            >
              X
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 등록 버튼 -->
    <div class="fixed-button">
      <button
        type="button"
        class="btn btn-secondary me-md-2"
        @click="this.$router.go(-1)"
      >
        취소
      </button>
      <button type="button" class="btn btn-primary" @click="saveFreeBoard">
        등록
      </button>
    </div>
  </div>
</template>

<script scope>
let daum = window.daum;
import BoardDetailService from "@/services/board/BoardDetailService";
import BoardWriteService from "@/services/board/BoardWriteService";

export default {
  data() {
    return {
      isAdmin:
        this.$store.state.member != null &&
        this.$store.state.member.memberCode === "AT01",
      bocode: "BO03", // 대분류 코드 고정a
      board: {
        boardTitle: "",
        boardContent: "",
        noticeYn: false, // 공지사항 초기값
      },
      vote: {
        voteName: "",
        voteList: {
          vote1: "",
          vote2: "",
          vote3: "",
          vote4: "",
          vote5: "",
        },
        delDate: "",
      },
      files: [], // 파일 리스트를 저장하기 위한 배열
      showSuccessMessage: false,
      submitted: false,
      address: null,
      map: null,
      geocoder: null,
      marker: null,

      badWords: ["ㅅㅂ", "ㅂㅅ", "욕설", "바보", "멍청이", "미친"],
    };
  },
  methods: {
    async saveFreeBoard() {
      try {
        let boardDto = {
          memberId: this.$store.state.member.memberId,
          boardTitle: this.board.boardTitle,
          boardContent: this.board.boardContent,
          bocode: this.bocode,
          smcode: this.smcode,
          noticeYn: this.board.noticeYn ? "Y" : "N",
        };

        let voteDtos = Object.values(this.vote.voteList)
          .filter((voteItem) => voteItem.trim() !== "")
          .map((voteItem) => ({
            voteName: this.vote.voteName,
            voteList: voteItem,
            delDate: this.vote.delDate,
          }));

        let placeDto = this.address ? { address: this.address } : null;

        let fileDtos = await Promise.all(
          this.files.map((file) => {
            return new Promise((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                resolve({
                  fileUrl: URL.createObjectURL(file.data),
                  fileName: file.name,
                  data: reader.result.split(",")[1], // Base64 문자열만 추출
                });
              };
              reader.onerror = reject;
              reader.readAsDataURL(file.data); // Base64 인코딩
            });
          })
        );

        let response = await BoardWriteService.create({
          boardDto,
          voteDtos: voteDtos.length > 0 ? voteDtos : null,
          placeDto: placeDto,
          fileDtos: fileDtos.length > 0 ? fileDtos : null,
        });

        console.log(response);
        this.submitted = true;
        alert("게시글이 등록되었습니다.");
        this.$router.push(`/board/free`);
      } catch (e) {
        console.log(e);
        alert("내용을 입력해주세요.");
      }
    },
    async retrieveCode() {
      try {
        let response = await BoardDetailService.getCmCd(this.smcode);
        this.cmcd = response.data;
        console.log(this.cmcd);
      } catch (e) {
        console.log("retrieveCode 에러", e);
      }
    },
    resetVoteForm() {
      this.vote = {
        voteName: "",
        voteList: {
          vote1: "",
          vote2: "",
          vote3: "",
          vote4: "",
          vote5: "",
        },
        delDate: "",
      };
      this.showSuccessMessage = false;
    },
    submitVote() {
      const voteItems = Object.values(this.vote.voteList).filter(
        (item) => item.trim() !== ""
      );
      if (voteItems.length < 2) {
        alert("투표 항목을 2개 이상 입력해주세요.");
        return;
      }
      if (!this.vote.delDate) {
        alert("투표 종료일을 선택해주세요.");
        return;
      }
      this.showSuccessMessage = true;
    },
    handleFileUpload(event) {
      const newFiles = Array.from(event.target.files);
      this.files = this.files.concat(
        newFiles.map((file) => ({
          name: file.name,
          data: file,
        }))
      );
    },
    removeFile(index) {
      this.files.splice(index, 1);
      if (this.files.length === 0) {
        this.$refs.fileInput.value = "";
      }
    },
    loadDaumPostcodeScript() {
      const script = document.createElement("script");
      script.src =
        "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
      document.head.appendChild(script);
    },
    loadKakaoMapScript() {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=55b411073309a73c48d56caa594311c8&libraries=services";
      script.onload = this.initMap;
      document.head.appendChild(script);
    },
    initMap() {
      const mapContainer = this.$refs.mapContainer;
      const mapOption = {
        center: new daum.maps.LatLng(37.537187, 127.005476),
        level: 5,
      };
      this.map = new daum.maps.Map(mapContainer, mapOption);
      this.geocoder = new daum.maps.services.Geocoder();
      this.marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(37.537187, 127.005476),
        map: this.map,
      });
    },
    openPostcode() {
      new daum.Postcode({
        oncomplete: (data) => {
          this.address = data.address;
          this.geocoder.addressSearch(data.address, (results, status) => {
            if (status === daum.maps.services.Status.OK) {
              const result = results[0];
              const coords = new daum.maps.LatLng(result.y, result.x);
              this.$refs.mapContainer.style.display = "block";
              this.map.relayout();
              this.map.setCenter(coords);
              this.marker.setPosition(coords);
            }
          });
        },
      }).open();
    },
    resetPlace() {
      this.address = "";
      if (this.map) {
        this.map.setCenter(new daum.maps.LatLng(37.537187, 127.005476));
        this.map.relayout();
        this.marker.setPosition(new daum.maps.LatLng(37.537187, 127.005476));
      }
      this.$refs.mapContainer.style.display = "none";
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
  watch: {
    "board.boardTitle": function (newValue) {
      this.board.boardTitle = this.filterBadWords(newValue);
    },
    "board.boardContent": function (newValue) {
      this.board.boardContent = this.filterBadWords(newValue);
    },
  },
  computed: {
    minDate() {
      const today = new Date();
      const tomorrow = new Date(today);
      tomorrow.setDate(tomorrow.getDate() + 1);
      return tomorrow.toISOString().slice(0, 10);
    },
  },
  mounted() {
    this.loadDaumPostcodeScript();
    this.loadKakaoMapScript();
    this.retrieveCode();
  },
};
</script>

<style>
.free-box {
  background-color: rgba(255, 255, 255, 1);
  padding: 40px 30px;
  border: 1px solid #959595;
  width: 1000px;
  position: relative; /* 부모 요소를 기준으로 위치를 설정합니다 */
  border-radius: 10px;
}

.fixed-button {
  position: absolute;
  bottom: -60px; /* free-box 아래에 위치를 조정 (간격 조정) */
  right: 0px; /* 오른쪽 끝에 위치를 조정 */
}

.file-list p {
  margin-bottom: 0;
}

.file-list .file-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>

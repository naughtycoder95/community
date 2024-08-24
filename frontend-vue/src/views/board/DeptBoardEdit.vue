<template>
  <div align="center">
    <h1 class="text-center mb-5 mt-5">부서 게시판</h1>
    <div class="container mt-3 free-box">
      <!-- 동호회 게시판 / 닉네임 입력 -->
      <div>
        <div class="row">
          <div class="col-4">
            <input
              type="text"
              class="form-control"
              disabled
              v-model="boardName"
            />
          </div>
          <div class="col-4">
            <input
              type="text"
              class="form-control"
              disabled
              v-model="smCmcd.cmCdName"
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
          <div class="col-md-2 d-flex align-items-center justify-content-end">
            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                v-model="isNoticeChecked"
              />
              <label class="form-check-label" for="noticeCheck">공지사항</label>
            </div>
          </div>
        </div>
        <!-- 투표 추가 -->
        <div class="d-grid gap-3 d-md-block mt-3 mb-3 text-start">
          <button
            type="button"
            class="btn btn-outline-dark me-3"
            data-bs-toggle="modal"
            data-bs-target="#vote-modal"
            v-if="this.vote"
          >
            <i class="bi bi-bar-chart-line"></i> 투표
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
                  <h1 class="modal-title fs-5" id="exampleModalLabel">투표</h1>
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
                    disabled
                    v-if="vote.length > 0"
                    v-model="vote[0].voteName"
                  />
                  <hr />
                  <h5 class="text-start">항목 보기</h5>
                  <div v-for="(data, index) in vote" :key="index">
                    <input
                      type="text"
                      class="form-control mb-3"
                      disabled
                      v-model="data.voteList"
                    />
                  </div>
                  <hr />
                  <h5 class="text-start">
                    종료일 :
                    <input
                      type="date"
                      v-if="vote.length > 0"
                      v-model="vote[0].delDate"
                      disabled
                    />
                  </h5>
                </div>
                <div class="modal-footer">
                  <button
                    type="button"
                    class="btn btn-primary"
                    data-bs-dismiss="modal"
                    @click="submitVote"
                  >
                    확인
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
            @click="relayout"
          >
            <i class="bi bi-geo-alt"></i> 장소
          </button>
          <!-- 장소 장소 보기Modal -->
          <div
            v-if="address"
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
                    style="width: 100%; height: 500px"
                    ref="map"
                  ></div>
                </div>
                <div class="modal-footer">
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
          <!-- 장소 장소 보기 Modal 끝 -->
          <!-- 장소 추가 Modal -->
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
                        class="form-control"
                        type="text"
                        v-model="address"
                        placeholder="주소검색 사용"
                        @keypress.enter="openPostcode"
                        disabled
                      />
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
                    @click="placeEdit"
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
      <div class="row" v-if="voteExists">
        <div class="col-md-12">
          <div class="alert alert-info" role="alert">
            {{ vote.voteName }}
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
            <!-- 새로 업로드된 파일 -->
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
            <!-- 기존 파일 -->
            <div
              v-for="(existingFile, index) in existingFiles"
              :key="'existing_' + index"
              class="file-item"
            >
              <p class="d-inline">{{ existingFile.fileName }}</p>
              <button
                type="button"
                class="btn btn-danger"
                @click="removeExistingFile(index)"
              >
                x
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 수정 버튼 -->
      <div class="fixed-button">
        <button type="button" class="btn btn-danger me-md-2" @click="editBoard">
          수정
        </button>
        <button
          type="button"
          class="btn btn-secondary"
          @click="this.$router.go(-1)"
        >
          취소
        </button>
      </div>
    </div>
  </div>
</template>

<script>
let daum = window.daum;
import BoardDetailService from "@/services/board/BoardDetailService";
import BoardEditService from "@/services/board/BoardEditService";
import BoardWrite from "@/services/board/BoardWriteService";
export default {
  components: {
    // KakaoMap,
  },
  data() {
    return {
      member: this.$store.state.member, // 현재 로그인된 회원 가져오기
      boardId: this.$route.params.boardId, // 현재 글 ID 가져오기
      smcode: this.$route.params.smcode, // 현재 소메뉴 코드 가져오기
      bocode: this.$route.params.bocode, // 현재 소메뉴 코드 가져오기

      auth: "", // 로그인 사용자 권한 체크
      memberInfo: "", // 회원정보
      board: {
        noticeYn: "",
        bocodeName: "", // board 객체에 bocodeName 속성 추가
      }, // 게시글
      boCmcd: {
        cmcdName: "", // cmcd 객체에 cmcdName 속성 추가
      }, // 부서코드, 부서명
      smCmcd: {},
      boardName: "부서 게시판",

      vote: [],
      voteExists: false, // 투표가 생성되었는지 여부를 저장하는 변수

      place: "", // 장소
      placeExists: false, // 장소가 등록되었는지 여부를 저장하는 변수
      boardImage: "", // 글 첨부 이미지
      files: [], // 파일 리스트를 저장하기 위한 배열

      selectBocode: "",
      existingFiles: [], // 기존 파일 목록을 저장하는 배열

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
      badWords: [
        "ㅅㅂ",
        "ㅂㅅ",
        "욕설",
        "바보",
        "멍청이",
        "미친",
      ],
    };
  },
  methods: {
    async retrieveMember() {
      try {
        let response = await BoardEditService.getMember(this.member.memberId);
        this.memberInfo = response.data;
        console.log("memberInfo 데이터 : ", response.data);
      } catch (e) {
        console.log("retrieveMember 에러", e);
      }
    },
    // 게시글, 작성자 정보 가져오기
    async retrieveBoard() {
      try {
        let response = await BoardEditService.getBoard(this.boardId);
        this.board = response.data;
        console.log("board 데이터 : ", response.data);
      } catch (e) {
        console.log("retrieveBoard 에러", e);
      }
    },
    // 코드번호로 코드명 가져오기 / 부서명
    async retrieveBocode() {
      try {
        let response = await BoardEditService.getCmCd(this.bocode); // DE01 코드를 가져오도록 수정
        // 영업팀 코드명을 board 객체에 할당합니다.
        this.boCmcd.cmcdName = response.data.cmCdName;
        console.log("부서코드명: ", this.boCmcd.cmcdName);
      } catch (e) {
        console.log("retrieveBocode 에러", e);
      }
    },
    async retrieveSmcode() {
      try {
        let response = await BoardEditService.getCmCd(this.smcode); // DE01 코드를 가져오도록 수정
        // 영업팀 코드명을 board 객체에 할당합니다.
        this.smCmcd = response.data;
      } catch (e) {
        console.log("retrieveSmcode 에러", e);
      }
    },
    // 글번호로 투표 가져오기
    async retrieveVote() {
      try {
        let response = await BoardDetailService.getVote(this.boardId);
        this.vote = response.data;
        console.log("투표 :", response);
        console.log("투표 :", this.vote);
      } catch (e) {
        console.log("vote 에러", e);
      }
    },
    relayout() {
      this.delayFunction(() => {
        this.map.relayout();
        this.retrievePlace(this.searchAddres);
      }, 160); // 1초 후 실행
      // this.map.relayout();
    },
    delayFunction(callback, delay) {
      setTimeout(callback, delay);
    },
    // 글번호로 장소를 가져오는 메서드
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
                '<div style="width:150px;text-align:center;padding:6px 0;">장소</div>',
            });
            infowindow.open(map, marker);

            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
          }
        });
      } catch (e) {
        console.log("장소를 표시할 수 없습니다.", e);
      }
    },
    // 카카오 API 호출하고, 장소 추가 후 확인 버튼 이벤트
    placeEdit() {
      this.placeExists = true;
      console.log("장소 어디?? : ", this.address.address);
    },
    // 글번호로 이미지 가져오기
    async retrieveImg() {
      try {
        let response = await BoardEditService.getImg(this.boardId);
        this.existingFiles = response.data; // 기존 파일 목록을 업데이트합니다.
        console.log("기존 이미지 데이터 : ", response.data);
      } catch (e) {
        console.log("retrieveImg 에러", e);
      }
    },
    // 파일추가
    handleFileUpload(event) {
      const newFiles = Array.from(event.target.files);
      this.files = this.files.concat(
        newFiles.map((file) => ({
          name: file.name,
          data: file,
        }))
      );
    },
    // 파일 선택 취소
    removeFile(index) {
      this.files.splice(index, 1);

      // 파일이 없을 경우 input 초기화
      if (this.files.length === 0) {
        this.$refs.fileInput.value = "";
      }
    },
    removeExistingFile(index) {
      this.existingFiles.splice(index, 1);
    },
    // 지도 API 위치
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
    execDaumPostcode() {
      new window.daum.Postcode({
        oncomplete: function (data) {
          var mapContainer = document.getElementById("map"), // 지도를 표시할 div
            mapOption = {
              center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
              level: 5, // 지도의 확대 레벨
            };
          var daum = window.daum;
          var map = new daum.maps.Map(mapContainer, mapOption);
          var marker = new daum.maps.Marker({
            position: new daum.maps.LatLng(37.537187, 127.005476),
            map: map,
          });
          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          document.getElementById("address").value = this.searchAddress;
          var geocoder = new daum.maps.services.Geocoder();
          // 주소로 상세 정보를 검색
          geocoder.addressSearch(data.address, function (results, status) {
            // 정상적으로 검색이 완료됐으면
            if (status === daum.maps.services.Status.OK) {
              var result = results[0]; //첫번째 결과의 값을 활용

              // 해당 주소에 대한 좌표를 받아서
              var coords = new daum.maps.LatLng(result.y, result.x);
              // 지도를 보여준다.
              mapContainer.style.display = "block";
              map.relayout();
              // 지도 중심을 변경한다.
              map.setCenter(coords);
              // 마커를 결과값으로 받은 위치로 옮긴다.
              marker.setPosition(coords);
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

    async editBoard() {
      try {
        // 임시 객체 변수
        // Board 테이블
        let boardDto = this.board;
        // Place 테이블
        let placeDto = this.address ? { address: this.address } : null;
        // File 테이블
        let fileDtos = await Promise.all(
          this.files.map((file) => {
            return new Promise((resolve, reject) => {
              const reader = new FileReader();
              reader.onload = () => {
                resolve({
                  // uuid: file.name,
                  fileUrl: file.fileUrl,
                  fileName: file.name,
                  data: reader.result.split(",")[1], // Base64 문자열만 추출
                });
              };
              reader.onerror = reject;
              reader.readAsDataURL(file.data); // Base64 인코딩
            });
          })
        );
        fileDtos = fileDtos
          .concat(this.existingFiles)
          .filter((file) => file.fileName);
        let response = await BoardWrite.update({
          boardDto,
          placeDto: placeDto,
          fileDtos: fileDtos.length > 0 ? fileDtos : null,
        });
        console.log(response);
        this.submitted = true;
        alert("게시글이 등록되었습니다.");
        this.$router.push(`/board/dept`);
      } catch (e) {
        console.log(e);
        alert("내용을 입력해주세요.");
      }
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
    isNoticeChecked: {
      get() {
        return this.board.noticeYn === "Y";
      },
      set(value) {
        this.board.noticeYn = value ? "Y" : "N";
      },
    },
  },

  mounted() {
    console.log(
      "부서코드 : ",
      this.smcode,
      "/ 글번호 : ",
      this.boardId,
      "/ 로그인ID : ",
      this.member.memberId
    );
    this.retrieveMember();
    this.retrieveBoard();
    this.retrieveBocode();
    this.retrieveSmcode();
    this.retrieveVote();
    this.retrieveImg();
    this.loadDaumPostcodeScript();
    this.loadKakaoMapScript();

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
  },
};
</script>

<style>
.free-box {
  margin: 100px auto;
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

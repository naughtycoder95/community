<template>
  <div>
    <input
      type="text"
      ref="keywordInput"
      @keyup.enter="searchPlaces"
      placeholder="장소 검색"
    />
    <div id="map" style="width: 100%; height: 500px" ref="map"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      map: null,
      infowindow: null,
      markers: [],
      options: {
        //지도를 생성할 때 필요한 기본 옵션
        center: {
          lat: 33.450701,
          lng: 126.570667,
        }, //지도의 중심좌표.
        level: 3, //지도의 레벨(확대, 축소 정도)
      },
    };
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.retrieveMap("부산 부산진구 중앙대로 749 4층");
    } else {
      const script = document.createElement("script");
      // 로드 완료 후 retrieveMap() 메서드 실행 추가
      script.onload = () => {
        this.retrieveMap("부산 부산진구 중앙대로 749 4층");
        // 지도 로드 완료 후 searchPlaces() 메서드 실행
        this.searchPlaces();
      };
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=55b411073309a73c48d56caa594311c8"; // 발급받은 API 키로 변경
      document.head.appendChild(script);
    }
  },
  methods: {
    retrieveMap(address) {
      let kakao = window.kakao;
      var container = this.$refs.map;
      const { center, level } = this.options;

      var map = new kakao.maps.Map(container, {
        center: new kakao.maps.LatLng(center.lat, center.lng),
        level,
      }); //지도 생성 및 객체 리턴
      map.relayout();

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      geocoder.addressSearch(address, function (result, status) {
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
              '<div style="width:150px;text-align:center;padding:6px 0;">모임장소</div>',
          });
          infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
        }
      });
    },
    //     initMap() {
    //       const container = document.getElementById("map");
    //       const options = {
    //         center: new kakao.maps.LatLng(37.566826, 126.9786567),
    //         level: 3,
    //       };
    //       this.map = new kakao.maps.Map(container, options);
    //       this.infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
    //     },

    //     // searchPlaces() 메서드 수정
    //     searchPlaces() {
    //       const keyword = this.$refs.keywordInput.value; // 검색어 입력 요소 참조

    //       if (!keyword.trim()) {
    //         alert("검색어를 입력해주세요.");
    //         return;
    //       }

    //       const ps = new kakao.maps.services.Places();
    //       ps.keywordSearch(keyword, (data, status, pagination) => {
    //         if (status === kakao.maps.services.Status.OK) {
    //           this.displayPlaces(data, pagination);
    //         } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    //           alert("검색 결과가 없습니다.");
    //         } else {
    //           alert("검색 중 오류가 발생했습니다.");
    //         }
    //       });
    //     },

    //     displayPlaces(places, pagination) {
    //       const fragment = document.createDocumentFragment();
    //       const bounds = new kakao.maps.LatLngBounds();

    //       this.removeMarkers(); // 기존 마커 제거

    //       for (let i = 0; i < places.length; i++) {
    //         const placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
    //         const marker = this.addMarker(placePosition);
    //         bounds.extend(placePosition);
    //       }

    //       this.map.setBounds(bounds);
    //     },

    //     addMarker(position) {
    //       const marker = new kakao.maps.Marker({
    //         position: position,
    //         map: this.map,
    //       });

    //       this.markers.push(marker);
    //       return marker;
    //     },

    //     removeMarkers() {
    //       for (let i = 0; i < this.markers.length; i++) {
    //         this.markers[i].setMap(null);
    //       }
    //       this.markers = [];
    //     },
  },
};
</script>

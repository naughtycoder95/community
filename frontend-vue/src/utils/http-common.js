// utils/http-common.js
// axios 기본 설정 : spring ip 주소 설정
import axios from "axios";

export default axios.create({
    // spring ip 주소
    // baseURL: "http://13.209.24.76:9000/api",
    baseURL: "http://localhost:9000/api",
    headers: {
        // 문서 종류
        "Content-Type": "application/json"
    }
})
import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween, randomString } from "https://jslib.k6.io/k6-utils/1.1.0/index.js";

export let options = {
    vus: 300, // 가상 사용자 수
    duration: '10m', // 테스트 지속 시간
};

export default function () {
    // 랜덤 데이터 생성
    const id = randomIntBetween(1, 1000000);
    const name = randomString(10);
    const age = randomIntBetween(1, 99);
    const address = `Random Street ${randomIntBetween(1, 1000)}`;

    // JSON 바디 구성
    const body = JSON.stringify({
        id: id,
        name: name,
        age: age,
        address: address
    });

    // HTTP POST 요청 설정
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // POST 요청 보내기
    let response = http.post('http://localhost:8080/api/people', body, params);

    // 응답 확인
    check(response, {
        'is status 200': (r) => r.status === 200,
    });
}

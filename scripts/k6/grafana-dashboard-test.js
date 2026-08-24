import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  stages: [
    { duration: '20s', target: 10 }, // 20초 동안 VU 10으로 워밍업
    { duration: '1m40s', target: 10 }, // 1분 40초 동안 VU 10 유지 (이 구간에서 대시보드 조작)
    { duration: '10s', target: 0  }, // 10초 동안 종료
  ],
};

// 도메인 주소 설정 (Nginx 공개주소/Tailscale Domain)
const BASE_URL = 'https://ip-172-31-33-118.tail1d9f85.ts.net';

export default function () {
  // 서버 에러가 해결된 이후에 다시 주석을 해제하자
  // // 1. 정상 경로 호출 (2xx -> QPS, 지연시간, Hikari/JVM 메트릭 유도)
  // // TODO: 실제 존재하는 상품 ID 번호(2자리 이상)를 지정하세요.
  // const res200 = http.get(`${BASE_URL}/products/string`);
  // check(res200, { 'status is 200': (r) => r.status === 200 });

  // // 2. 에러 경로 호출 (4xx -> Loki 로그, logback_events_total 유도)
  // // 10번 중 2번 꼴로 에러 요청 생성
  // if (__ITER % 5 === 0) {
  //   const res400 = http.get(`${BASE_URL}/products/1`);
  //   check(res400, { 'status is 400': (r) => r.status === 400 });
  // }

  // // 과도한 CPU 점유를 막고 10초 스크랩 주기와 호흡을 맞추기 위한 휴식
  // sleep(1);
  
  // k6 연습
  const res400 = http.get(`${BASE_URL}/products/1`);
  check(res400, { 'status is 400': (r) => r.status === 400 });

  // 과도한 CPU 점유를 막고 10초 스크랩 주기와 호흡을 맞추기 위한 휴식
  sleep(1);
}

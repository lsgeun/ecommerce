import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 5,         // 가상 유저 5명
  duration: '5s', // 5초 동안 부하 유지
};

export default function () {
  const res = http.get('http://localhost:8080/products/1');

  check(res, {
    'status가 400인가?': (r) => r.status === 400,
  });

  sleep(1);
}

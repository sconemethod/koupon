import fetch from 'node-fetch';

const CONCURRENT_USERS = 200;
const API_URL = 'http://localhost:4000/coupon/issue';

async function issueCoupon(userId, eventId) {
  const res = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userId, eventId })
  });

  const text = await res.text();
  console.log(`[${userId}] ${res.status} ${text}`);
}

(async () => {
  const eventId = 9; // 테스트할 이벤트 ID
  const users = Array.from({ length: CONCURRENT_USERS }, (_, i) => `testUser${i + 1}`);
  await Promise.all(users.map(userId => issueCoupon(userId, eventId)));
})();

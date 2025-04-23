import fetch from 'node-fetch';

const USER_COUNT = 200;
const API_URL = 'http://localhost:4000/auth/login';

async function registerUser(userId) {
  const res = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userId }),
  });

  const text = await res.text();
  console.log(`[${userId}] ${res.status} ${text}`);
}

(async () => {
  const users = Array.from({ length: USER_COUNT }, (_, i) => `testUser${i + 1}`);
  await Promise.all(users.map(userId => registerUser(userId)));
})();

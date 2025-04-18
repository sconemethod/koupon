// 📁 src/utils/cookie.ts

export function getUserIdFromCookie(): string | null {
  const match = document.cookie.match(/(?:^|; )userId=([^;]*)/);
  return match ? decodeURIComponent(match[1]) : null;
}

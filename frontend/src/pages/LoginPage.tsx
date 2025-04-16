// koupon/frontend/src/pages/LoginPage.tsx
import { useState, useEffect } from "react";

const LoginPage = () => {
  const [userId, setUserId] = useState("");
  const [savedUserId, setSavedUserId] = useState<string | null>(null);

  // 쿠키에서 userId 가져오기
  useEffect(() => {
    const match = document.cookie.match(/userId=([^;]+)/);
    if (match) setSavedUserId(match[1]);
  }, []);

  // 로그인 처리
  const handleLogin = async () => {
    if (!userId) return alert("userId를 입력해주세요!");

    // 쿠키 저장
    document.cookie = `userId=${userId}; path=/;`;

    // 서버에 로그인 기록 전송
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId }),
        credentials: "include"
      });
      const result = await res.text();
      alert(`로그인 완료! 서버 응답: ${result}`);
      setSavedUserId(userId);
    } catch (e) {
      alert("로그인 실패!");
      console.error(e);
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Login Page</h2>
      {savedUserId ? (
        <p>✅ 현재 로그인된 userId: <strong>{savedUserId}</strong></p>
      ) : (
        <>
          <input
            type="text"
            placeholder="userId 입력"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          />
          <button onClick={handleLogin}>로그인</button>
        </>
      )}

      <hr />

      {/* 🎁 나중에 테이블 영역 - 로그인 기록 */}
      <h3>로그인 기록 (예시 테이블)</h3>
      <table border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>user_id</th>
            <th>로그인 시각</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>user123</td>
            <td>2025-04-16 16:12</td>
          </tr>
          {/* 실제로는 백엔드에서 불러온 데이터로 매핑 예정 */}
        </tbody>
      </table>
    </div>
  );
};

export default LoginPage;

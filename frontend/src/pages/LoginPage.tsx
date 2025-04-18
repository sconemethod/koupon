import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

const LoginPage = () => {
  const [userId, setUserId] = useState("");
  const [savedUserId, setSavedUserId] = useState<string | null>(null);

  // 로그인 쿠키 확인
  useEffect(() => {
    const match = document.cookie.match(/userId=([^;]+)/);
    if (match) setSavedUserId(match[1]);
  }, []);

  // 로그인
  const handleLogin = async () => {
    if (!userId) return alert("userId를 입력해주세요!");
    document.cookie = `userId=${userId}; path=/;`;
    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/auth/login`, {
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

  // 로그아웃
  const handleLogout = () => {
    document.cookie = "userId=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    setSavedUserId(null);
    alert("로그아웃 완료!");
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Login Page</h2>
      {savedUserId ? (
        <>
          <p>✅ 현재 로그인된 userId: <strong>{savedUserId}</strong></p>
          <button onClick={handleLogout}>로그아웃</button>
        </>
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
      <Link to="/events">🎁 이벤트 보러 가기</Link>
      <hr />

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
        </tbody>
      </table>
    </div>
  );
};

export default LoginPage;

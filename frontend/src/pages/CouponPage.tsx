// 📁 koupon/frontend/src/pages/CouponPage.tsx

import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";

const CouponPage = () => {
  const [now, setNow] = useState(new Date());

  const [startTime, setStartTime] = useState(() => {
    const now = new Date();
    return now; // 지금부터 시작
  });

  const [endTime, setEndTime] = useState(() => {
    const end = new Date();
    end.setHours(end.getHours() + 1); // 한 시간 뒤 종료
    return end;
  });

  const [eventOpened, setEventOpened] = useState(false);
  const [message, setMessage] = useState("");

  const handleGetCoupon = async () => {
    const res = await fetch("/coupon/issue", {
      method: "POST",
      credentials: "include",
    });

    const text = await res.text();

    if (res.ok) {
      setMessage(`🎉 쿠폰 발급 완료: ${text}`);
    } else {
      setMessage(`😢 ${text}`);
    }
  };

  useEffect(() => {
    const timer = setInterval(() => setNow(new Date()), 1000);
    return () => clearInterval(timer);
  }, []);

  useEffect(() => {
    setEventOpened(now >= startTime && now <= endTime);
  }, [now, startTime, endTime]);

  return (
    <div style={{ padding: "2rem" }}>
      <nav style={{ marginBottom: "1rem" }}>
        <Link to="/">🏠 로그인으로 돌아가기</Link>
      </nav>

      <h2>Coupon Page</h2>
      {!eventOpened ? (
        <p>⏳ 이벤트 시작까지: {Math.floor((startTime.getTime() - now.getTime()) / 1000)}초</p>
      ) : (
        <button onClick={handleGetCoupon}>🎁 쿠폰 받기</button>
      )}
      <p style={{ marginTop: "1rem" }}>{message}</p>
    </div>
  );
};

export default CouponPage;
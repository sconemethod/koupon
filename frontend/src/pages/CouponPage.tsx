// 📁 pages/CouponPage.tsx

import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { getUserIdFromCookie } from "../utils/cookie";

const CouponPage = () => {
  const { id: eventId } = useParams();
  const [message, setMessage] = useState("");

  const handleGetCoupon = async () => {
    const userId = getUserIdFromCookie();

    const res = await fetch(`${import.meta.env.VITE_API_URL}/coupon/issue`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ userId, eventId: Number(eventId) })
    });

    const text = await res.text();
    setMessage(res.ok ? `🎉 ${text}` : `😢 ${text}`);
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>🎁 쿠폰 발급 페이지</h2>
      <button onClick={handleGetCoupon}>쿠폰 받기</button>
      <p>{message}</p>
    </div>
  );
};

export default CouponPage;

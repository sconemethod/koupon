// 📁 koupon/frontend/src/pages/AdminEventPage.tsx

import React, { useState } from "react";

const AdminEventPage = () => {
  const [eventName, setEventName] = useState("");
  const [description, setDescription] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [totalQuantity, setTotalQuantity] = useState(100);
  const [creatorUserId, setCreatorUserId] = useState("");
  const [message, setMessage] = useState("");

  const handleCreateEvent = async () => {
    const res = await fetch(`${import.meta.env.VITE_API_URL}/admin/event`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        eventName,         // 여기선 문제 없음!
        description,
        startTime,
        endTime,
        totalQuantity,
        creatorUserId
      })
    });
  
    const text = await res.text();
    setMessage(res.ok ? `✅ ${text}` : `❌ ${text}`);
  };
  

  return (
    <div style={{ padding: "2rem" }}>
      <h2>📋 쿠폰 이벤트 등록</h2>

     
      <br /><br />

      <label>📛 이벤트 이름:</label>
      <input type="text" value={eventName} onChange={(e) => setEventName(e.target.value)} />

      <br /><br />

      <label>📝 이벤트 설명:</label>
      <textarea value={description} onChange={(e) => setDescription(e.target.value)} />

      <br /><br />

      <label>🕒 시작 시간:</label>
      <input type="datetime-local" value={startTime} onChange={(e) => setStartTime(e.target.value)} />

      <br /><br />

      <label>🕒 종료 시간:</label>
      <input type="datetime-local" value={endTime} onChange={(e) => setEndTime(e.target.value)} />

      <br /><br />

      <label>📦 총 수량:</label>
      <input type="number" value={totalQuantity} onChange={(e) => setTotalQuantity(Number(e.target.value))} />

      <br /><br />

      <label>👤 만든 사람 (userId):</label>
      <input type="text" value={creatorUserId} onChange={(e) => setCreatorUserId(e.target.value)} />

      <br /><br />

      <button onClick={handleCreateEvent}>이벤트 생성하기</button>

      <p style={{ marginTop: "1rem" }}>{message}</p>
    </div>
  );
};

export default AdminEventPage;

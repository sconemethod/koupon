// 📁 koupon/frontend/src/pages/EventEditPage.tsx

import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const EventEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [eventData, setEventData] = useState({
    eventName: "",
    description: "",
    startTime: "",
    endTime: "",
    totalQuantity: 0,
  });

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/events/${id}`)
      .then(res => res.json())
      .then(data => setEventData(data))
      .catch(err => console.error("이벤트 조회 실패:", err));
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setEventData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    console.log("🛰️ 수정 요청 보냄:", { id, eventData });
  
    const res = await fetch(`${import.meta.env.VITE_API_URL}/admin/event/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(eventData)
    });
  
    const text = await res.text(); // 에러 메시지 출력
    console.log("📩 응답 내용:", text);
  
    if (res.ok) {
      alert("이벤트가 성공적으로 수정되었습니다!");
      navigate("/events");
    } else {
      alert("😢 수정 실패: " + text);
    }
  };
  


// ✅ 제출 버튼 → PUT 요청 전송
fetch(`${import.meta.env.VITE_API_URL}/admin/event/${id}`, {
  method: "PUT",
  headers: {
    "Content-Type": "application/json",
    // 관리자 보호용 토큰도 여기 붙일 수 있음
    // "Authorization": `Bearer ${yourAdminToken}`
  },
  body: JSON.stringify(eventData)
});

    
  return (
    <div style={{ padding: "2rem" }}>

  <h2>🛠️ 이벤트 수정</h2>

      <label>📛 이름:</label>
      <input type="text" name="eventName" value={eventData.eventName} onChange={handleChange} readOnly />
      <br /><br />

      <label>📝 설명:</label>
      <textarea name="description" value={eventData.description} onChange={handleChange} />
      <br /><br />

      <label>🕒 시작 시간:</label>
      <input type="datetime-local" name="startTime" value={eventData.startTime} onChange={handleChange} />
      <br /><br />

      <label>🕒 종료 시간:</label>
      <input type="datetime-local" name="endTime" value={eventData.endTime} onChange={handleChange} />
      <br /><br />

      <label>📦 총 수량:</label>
      <input type="number" name="totalQuantity" value={eventData.totalQuantity} onChange={handleChange} />
      <br /><br />

      <button onClick={handleSubmit}>💾 수정하기</button>
    </div>
  );
};

export default EventEditPage;

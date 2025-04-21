import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getUserIdFromCookie } from "../utils/cookie";

interface Event {
  eventId: number;
  eventName: string;
  description: string;
  startTime: string;
  endTime: string;
}

const EventDetailPage = () => {
  const { id } = useParams();
  const [event, setEvent] = useState<Event | null>(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/events/${id}`)
      .then(res => res.json())
      .then(data => setEvent(data));
  }, [id]);

  const handleGetCoupon = async () => {
    const userId = getUserIdFromCookie();

    const res = await fetch(`${import.meta.env.VITE_API_URL}/coupon/issue`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ userId, eventId: Number(id) })
    });

    const text = await res.text();
    setMessage(res.ok ? `🎉 ${text}` : `😢 ${text}`);
  };

  if (!event) return <p>로딩 중...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>{event.eventName}</h2>
      <p>{event.description}</p>
      <p>🕒 {event.startTime} ~ {event.endTime}</p>

      <button onClick={handleGetCoupon}>🎁 이 이벤트에서 쿠폰 받기</button>
      <p style={{ marginTop: "1rem" }}>{message}</p>
    </div>
  );
};

export default EventDetailPage;

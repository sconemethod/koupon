// 📁 pages/EventDetailPage.tsx

import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

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

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/events/${id}`)
      .then(res => res.json())
      .then(data => setEvent(data));
  }, [id]);

  if (!event) return <p>로딩 중...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>{event.eventName}</h2>
      <p>{event.description}</p>
      <p>🕒 {event.startTime} ~ {event.endTime}</p>

      <Link to={`/events/${event.eventId}/coupon`}>
        <button>🎁 이 이벤트에서 쿠폰 받기</button>
      </Link>
    </div>
  );
};

export default EventDetailPage;

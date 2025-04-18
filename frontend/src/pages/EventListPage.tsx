// 📁 pages/EventListPage.tsx

import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

interface Event {
  eventId: number;
  eventName: string;
  description: string;
  startTime: string;
  endTime: string;
    }
const EventListPage = () => {
  const [events, setEvents] = useState<Event[]>([]);

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/events`)
      .then(res => res.json())
      .then(data => setEvents(data));
  }, []);

  return (
    <div style={{ padding: "2rem" }}>
      <h2>📋 진행 중인 이벤트</h2>
      <ul>
        {events.map(event => (
          <li key={event.eventId}>
            <Link to={`/events/${event.eventId}`}>{event.eventName}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default EventListPage;

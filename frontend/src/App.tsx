// koupon/frontend/src/App.tsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import AdminEventPage from "./pages/AdminEventPage";
import EventListPage from "./pages/EventListPage";
import EventDetailPage from "./pages/EventDetailPage";
import EventEditPage from "./pages/EventEditPage";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/admin/event" element={<AdminEventPage />} />
        <Route path="/admin/event/edit/:id" element={<EventEditPage />} />
        <Route path="/events" element={<EventListPage />} />
        <Route path="/events/:id" element={<EventDetailPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;

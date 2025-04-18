// koupon/frontend/src/App.tsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import CouponPage from "./pages/CouponPage";
import AdminEventPage from "./pages/AdminEventPage";
import EventListPage from "./pages/EventListPage";
import EventDetailPage from "./pages/EventDetailPage";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/admin/event" element={<AdminEventPage />} />
        <Route path="/events" element={<EventListPage />} />
        <Route path="/events/:id" element={<EventDetailPage />} />
        <Route path="/events/:id/coupon" element={<CouponPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;

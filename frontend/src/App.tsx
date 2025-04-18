// koupon/frontend/src/App.tsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import CouponPage from "./pages/CouponPage";
import AdminEventPage from "./pages/AdminEventPage";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/coupon" element={<CouponPage />} />
        <Route path="/admin/event" element={<AdminEventPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;

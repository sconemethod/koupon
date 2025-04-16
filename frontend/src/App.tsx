// koupon/frontend/src/App.tsx

import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        {/* 다음엔 /coupon, /admin, /mypage 추가 예정 */}
      </Routes>
    </BrowserRouter>
  );
};

export default App;

import React from "react";
import { useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    try {
      localStorage.removeItem("auth");
    } catch (_) {}
    navigate("/login");
  };

  return (
    <header className="bg-white border-b shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 py-3 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="h-9 w-9 rounded-xl bg-indigo-100 flex items-center justify-center">💰</div>
          <span className="font-bold text-lg">Finance App</span>
        </div>
        <div className="flex items-center gap-3">
          <button
            onClick={() => navigate("/dashboard")} 
            className="text-sm text-gray-700 hover:text-gray-900"
          >Dashboard</button>
          <button
            onClick={handleLogout}
            className="px-3 py-1.5 rounded-lg bg-gray-900 text-white text-sm hover:bg-black"
          >Logout</button>
        </div>
      </div>
    </header>
  );
}

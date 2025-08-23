// App.jsx
import { useState } from "react";
import Signup from "./components/Signup";
import Login from "./components/Login";
import Dashboard from "./components/dashboard/Dashboard";

export default function App() {
  const [userId, setUserId] = useState(null);

  return (
    <div className="min-h-screen bg-gray-50 font-sans">
      {!userId ? (
        <div className="max-w-md mx-auto pt-12">
          <h1 className="text-3xl font-bold text-center text-indigo-600 mb-8">
            Finance App 💰
          </h1>
          <div className="bg-white shadow-lg rounded-2xl p-6 space-y-8">
            <Signup setUserId={setUserId} />
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="bg-white px-2 text-gray-500">Or</span>
              </div>
            </div>
            <Login setUserId={setUserId} />
          </div>
        </div>
      ) : (
        <Dashboard userId={userId} />
      )}
    </div>
  );
}

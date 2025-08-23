// App.jsx
import React, { useState } from "react";
import Login from "./components/Login";
import Signup from "./components/Signup";
import AuthLayout from "./components/AuthLayout";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showSignup, setShowSignup] = useState(false);

  const handleAuthSuccess = (data) => {
    console.log("Auth success:", data);
    setIsLoggedIn(true);
  };

  if (isLoggedIn) {
    return (
      <div className="flex items-center justify-center h-screen bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500">
        <div className="bg-white shadow-lg rounded-2xl p-10 text-center w-[400px]">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">🎉 Welcome!</h2>
          <p className="text-gray-600">You’re successfully logged in.</p>
        </div>
      </div>
    );
  }

  return (
    <AuthLayout>
      {showSignup ? (
        <Signup
          onSignupSuccess={handleAuthSuccess}
          onShowLogin={() => setShowSignup(false)}
        />
      ) : (
        <Login
          onLoginSuccess={handleAuthSuccess}
          onShowSignup={() => setShowSignup(true)}
        />
      )}
    </AuthLayout>
  );
}

export default App;

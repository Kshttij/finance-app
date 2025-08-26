// src/App.jsx
import React, { useState } from "react";
import Login from "./pages/Auth/Login";
import Signup from "./pages/Auth/Signup";
import AuthLayout from "./pages/Auth/AuthLayout";
import Dashboard from "./pages/Dashboard";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showSignup, setShowSignup] = useState(false);
  const [userData, setUserData] = useState(null);

  const handleAuthSuccess = (data) => {
    console.log("Auth success:", data);
    setUserData(data);
    setIsLoggedIn(true);
  };

  if (isLoggedIn) {
    return <Dashboard user={userData} />;
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

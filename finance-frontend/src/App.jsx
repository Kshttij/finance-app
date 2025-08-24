// App.jsx
import React, { useState } from "react";
import Login from "./components/Login";
import Signup from "./components/Signup";
import AuthLayout from "./components/AuthLayout";
import Dashboard from "./components/dashboard/Dashboard"; // ✅ correct path

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

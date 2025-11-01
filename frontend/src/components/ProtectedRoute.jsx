// src/components/ProtectedRoute.jsx
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  // ✅ Check for the token, not the username!
  const token = localStorage.getItem("token");

  if (!token) {
    // If no token, redirect to login
    return <Navigate to="/login" replace />;
  }
  
  // If token exists, show the children (the protected page)
  return children;
};

export default ProtectedRoute;
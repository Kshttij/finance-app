// src/pages/AuthPage.jsx
import { useState } from "react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { login, register } from "../api"; // ✅ Use our new API functions
import "../index.css";

const AuthPage = () => {
  const [isSignup, setIsSignup] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  // ❌ const [email, setEmail] = useState(""); // <-- REMOVED
  const navigate = useNavigate();

  const toggleMode = () => setIsSignup(!isSignup);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // ✅ Create the payload (username and password only)
    const credentials = { username, password };
    
    // ❌ REMOVED 'if (isSignup)' block that added email

    try {
      const { data } = isSignup
        ? await register(credentials) // ✅ Now sends {username, password}
        : await login(credentials);   // ✅ Use API

      // ✅ Store both token and username from the response
      // (This assumes your /register endpoint ALSO returns a token,
      // just like your /login endpoint)
      localStorage.setItem("token", data.token);
      localStorage.setItem("userName", JSON.stringify(username));

      toast.success(data.message || "Success!");
      navigate("/", { replace: true }); // Redirect to dashboard

    } catch (err) {
      toast.error(err.response?.data?.message || "Something went wrong!");
    }
  };

  return (
    <div className="auth-page">
      <h1>{isSignup ? "Create Account" : "Login"}</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        {/* ❌ REMOVED the email input field */}

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit" className="btn btn--dark">
          {isSignup ? "Sign Up" : "Login"}
        </button>
      </form>
      <p onClick={toggleMode} style={{ cursor: "pointer", marginTop: "1rem" }}>
        {isSignup ? "Already have an account? Login" : "New here? Create account"}
      </p>
    </div>
  );
};

export default AuthPage;
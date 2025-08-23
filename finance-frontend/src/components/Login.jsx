import { useState } from "react";

export default function Login({ setUserId }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMessage(""); // reset previous errors

    try {
  const res = await fetch(`${import.meta.env.VITE_API_URL}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });


      const data = await res.json(); // always parse JSON

      if (res.ok) {
        setUserId(data.id); // set user ID in parent
        alert(data.message); // optional success message
      } else {
        setErrorMessage(data.message || "Login failed"); // show backend error
      }
    } catch (err) {
      console.error("Login error:", err);
      setErrorMessage("An unexpected error occurred");
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <h2>Login</h2>

      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
        required
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
        required
      />

      <button type="submit">Login</button>

      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
    </form>
  );
}

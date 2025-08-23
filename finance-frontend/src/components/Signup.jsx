import { useState } from "react";

export default function Signup({ setUserId }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();
    setErrorMessage(""); // reset previous errors

   try {
  const res = await fetch(`${import.meta.env.VITE_API_URL}/api/auth/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password }),
  });
      const data = await res.json();

      if (res.ok) {
        setUserId(data.id); // set user ID in parent
        alert(data.message || "Signup successful"); // optional success message
      } else {
        setErrorMessage(data.message || "Signup failed");
      }
    } catch (err) {
      console.error("Signup error:", err);
      setErrorMessage("An unexpected error occurred");
    }
  };

  return (
    <form onSubmit={handleSignup}>
      <h2>Signup</h2>

      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button type="submit">Signup</button>

      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
    </form>
  );
}

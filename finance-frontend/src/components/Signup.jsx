// Signup.jsx
import { useState } from "react";

export default function Signup({ setUserId }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    try {
      const res = await fetch(`${import.meta.env.VITE_API_URL}/api/auth/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, password }),
      });
      const data = await res.json();

      if (res.ok) {
        setUserId(data.id);
        alert(data.message || "Signup successful");
      } else {
        setErrorMessage(data.message || "Signup failed");
      }
    } catch (err) {
      console.error("Signup error:", err);
      setErrorMessage("An unexpected error occurred");
    }
  };

  return (
    <form onSubmit={handleSignup} className="space-y-4">
      <h2 className="text-xl font-semibold text-gray-800">Create Account</h2>
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-400"
        required
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-400"
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-indigo-400"
        required
      />
      <button
        type="submit"
        className="w-full bg-indigo-600 text-white py-2 rounded-lg hover:bg-indigo-700 transition"
      >
        Signup
      </button>
      {errorMessage && <p className="text-red-500 text-sm">{errorMessage}</p>}
    </form>
  );
}

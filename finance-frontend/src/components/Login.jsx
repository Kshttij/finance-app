import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/api";

function Login({ onLoginSuccess, onShowSignup }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) =>
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const data = await login({ email: form.email, password: form.password });
      if (data && (data.id || data.message || data.token)) {
        onLoginSuccess(data);
      } else {
        setError(data?.message || "Login failed");
      }
    } catch (err) {
      setError("Unable to login. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white/95 backdrop-blur p-8 rounded-2xl shadow-2xl border border-gray-100 w-full max-w-md">
      <div className="text-center mb-6">
        <div className="mx-auto h-12 w-12 rounded-xl bg-blue-100 flex items-center justify-center">
          <span className="text-xl">🔐</span>
        </div>
        <h2 className="mt-4 text-2xl font-bold tracking-tight text-gray-900">
          Welcome back
        </h2>
        <p className="text-sm text-gray-500">Log in to continue</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Email
          </label>
          <input
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            required
            className="w-full rounded-lg border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-shadow shadow-sm hover:shadow-md"
            placeholder="you@example.com"
            autoComplete="email"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Password
          </label>
          <input
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            required
            className="w-full rounded-lg border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-shadow shadow-sm hover:shadow-md"
            placeholder="••••••••"
            autoComplete="current-password"
          />
        </div>

        {error && (
          <div className="text-red-600 text-sm bg-red-50 border border-red-200 rounded-md p-2 transition-all animate-fade-in">
            {error}
          </div>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-lg bg-blue-600 text-white py-2.5 font-medium hover:bg-blue-700 active:scale-95 transition-all duration-150 disabled:opacity-60 shadow-md hover:shadow-lg"
        >
          {loading ? "Logging in..." : "Log In"}
        </button>
      </form>

      <div className="text-center mt-4 text-sm">
        Don’t have an account?{" "}
        <button
          type="button"
          onClick={() => onShowSignup(true)}
          className="text-blue-600 hover:underline font-medium"
        >
          Create one
        </button>
      </div>
    </div>
  );
}

export default Login;

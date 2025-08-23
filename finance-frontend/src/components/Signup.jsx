import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signup } from "../services/api";

function Signup({ onSignupSuccess, onShowLogin }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [okMsg, setOkMsg] = useState("");

  const handleChange = (e) =>
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setOkMsg("");
    setLoading(true);
    try {
      const data = await signup({
        name: form.name,
        email: form.email,
        password: form.password,
      });
      if (data && (data.id || data.message)) {
        setOkMsg(data.message || "Signup successful!");
        onSignupSuccess(data);
      } else {
        setError(data?.message || "Signup failed");
      }
    } catch (err) {
      setError("Unable to signup. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white/95 backdrop-blur p-8 rounded-2xl shadow-2xl border border-gray-100 w-full max-w-md">
      <div className="text-center mb-6">
        <div className="mx-auto h-12 w-12 rounded-xl bg-emerald-100 flex items-center justify-center">
          <span className="text-xl">🧾</span>
        </div>
        <h2 className="mt-4 text-2xl font-bold tracking-tight text-gray-900">
          Create your account
        </h2>
        <p className="text-sm text-gray-500">Start tracking your finances</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Full name
          </label>
          <input
            name="name"
            type="text"
            value={form.name}
            onChange={handleChange}
            required
            className="w-full rounded-lg border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-shadow shadow-sm hover:shadow-md"
            placeholder="Jane Doe"
            autoComplete="name"
          />
        </div>

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
            className="w-full rounded-lg border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-shadow shadow-sm hover:shadow-md"
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
            className="w-full rounded-lg border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-shadow shadow-sm hover:shadow-md"
            placeholder="Create a strong password"
            autoComplete="new-password"
          />
        </div>

        {error && (
          <div className="text-red-600 text-sm bg-red-50 border border-red-200 rounded-md p-2 transition-all animate-fade-in">
            {error}
          </div>
        )}
        {okMsg && (
          <div className="text-emerald-700 text-sm bg-emerald-50 border border-emerald-200 rounded-md p-2 transition-all animate-fade-in">
            {okMsg}
          </div>
        )}

        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-lg bg-emerald-600 text-white py-2.5 font-medium hover:bg-emerald-700 active:scale-95 transition-all duration-150 disabled:opacity-60 shadow-md hover:shadow-lg"
        >
          {loading ? "Creating account..." : "Create account"}
        </button>
      </form>

      <div className="text-center mt-4 text-sm">
        Already have an account?{" "}
        <button
          type="button"
          onClick={onShowLogin}
          className="text-blue-600 hover:underline font-medium"
        >
          Log in
        </button>
      </div>
    </div>
  );
}

export default Signup;

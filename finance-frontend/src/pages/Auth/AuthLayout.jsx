// components/AuthLayout.jsx
import React from "react";

export default function AuthLayout({ children }) {
  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2">
      {/* Left side: Form */}
      <div className="flex flex-col justify-center items-center bg-gray-50 p-8">
        <h1 className="text-3xl font-bold text-indigo-600 mb-6">Finance App 💰</h1>
        <div className="w-full max-w-md bg-white shadow-lg rounded-2xl p-6">
          {children}
        </div>
      </div>

      {/* Right side: Illustration */}
      <div className="hidden lg:flex items-center justify-center bg-indigo-600 text-white p-12">
        <div className="text-center space-y-6">
          <h2 className="text-4xl font-bold">Manage your money smarter</h2>
          <p className="text-lg">
            Track expenses, save better, and grow your wealth 🚀
          </p>
          <img
            src="https://illustrations.popsy.co/yellow/keynote-presentation.svg"
            alt="Finance Illustration"
            className="max-w-md mx-auto"
          />
        </div>
      </div>
    </div>
  );
}

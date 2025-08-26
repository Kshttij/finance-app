import React from "react";

export default function SummaryCard({ title, amount, tone = "neutral" }) {
  const isPositive = tone === "positive";
  const isNegative = tone === "negative";
  const bg = isPositive ? "bg-emerald-50" : isNegative ? "bg-rose-50" : "bg-gray-50";
  const text = isPositive ? "text-emerald-700" : isNegative ? "text-rose-700" : "text-gray-700";

  return (
    <div className={`rounded-2xl ${bg} p-4 border`}> 
      <div className="text-sm text-gray-500">{title}</div>
      <div className={`text-2xl font-bold ${text}`}>₹{Number(amount || 0).toLocaleString()}</div>
    </div>
  );
}

import React from "react";

export default function SummaryCard({ title, amount, color }) {
  return (
    <div className={`flex-1 ${color} shadow rounded p-4 text-center`}>
      <h3 className="text-lg font-medium mb-2">{title}</h3>
      <p className="text-2xl font-bold">{amount}</p>
    </div>
  );
}

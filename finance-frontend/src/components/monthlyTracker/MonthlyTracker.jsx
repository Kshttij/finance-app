import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import FilterControls from "../tools/FilterControls";

export default function MonthlyTracker({ userId }) {
  const [transactions, setTransactions] = useState([]);
  const [dateRange, setDateRange] = useState({
    start: "2025-08-01T00:00:00Z", // default example
    end: "2025-08-31T23:59:59Z"
  });

  useEffect(() => {
    if (!userId) return;

    axios
      .get(`http://localhost:8080/api/transactions/${userId}/monthly`, {
        params: {
          startOfMonth: dateRange.start,
          endOfMonth: dateRange.end
        }
      })
      .then((res) => setTransactions(res.data))
      .catch((err) => console.error("Error fetching monthly transactions:", err));
  }, [userId, dateRange]);

  return (
    <div className="p-4 border rounded-lg shadow bg-white">
      <h2 className="text-lg font-bold mb-2">Monthly Tracker</h2>

      {/* Filters */}
      <FilterControls dateRange={dateRange} setDateRange={setDateRange} />

      {/* Transactions List */}
      <ul className="mt-4 space-y-2">
        {transactions.map((t) => (
          <li key={t.id} className="border p-2 rounded">
            <span className="font-medium">{t.category}</span>: {t.amount} ({t.type})
          </li>
        ))}
      </ul>
    </div>
  );
}

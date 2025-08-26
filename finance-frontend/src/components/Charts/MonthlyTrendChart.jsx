import React, { useEffect, useMemo, useState } from "react";
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from "recharts";
import { getMonthlyTransactions } from "../../services/api";

// This chart plots **daily totals** for the selected range's month using backend /monthly endpoint.
export default function MonthlyTrendChart({ userId, start, end }) {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  // Compute month bounds based on current range start
  const monthStart = useMemo(() => new Date(start.getFullYear(), start.getMonth(), 1, 0, 0, 0, 0), [start]);
  const monthEnd = useMemo(() => new Date(start.getFullYear(), start.getMonth() + 1, 0, 23, 59, 59, 999), [start]);

  useEffect(() => {
    if (!userId || !start || !end) return;
    const run = async () => {
      setLoading(true);
      try {
        const res = await getMonthlyTransactions(userId, monthStart.toISOString(), monthEnd.toISOString());
        const txs = Array.isArray(res) ? res : [];
        // Aggregate by day
        const byDay = new Map(); // key: dd
        for (const t of txs) {
          const d = new Date(t.date);
          const dd = d.getDate();
          if (!byDay.has(dd)) byDay.set(dd, { day: dd, income: 0, expense: 0 });
          const row = byDay.get(dd);
          if ((t?.type || "").toUpperCase() === "INCOME") row.income += Number(t.amount || 0);
          else if ((t?.type || "").toUpperCase() === "EXPENSE") row.expense += Number(t.amount || 0);
        }
        const daysInMonth = monthEnd.getDate();
        const final = [];
        for (let dd = 1; dd <= daysInMonth; dd++) final.push(byDay.get(dd) || { day: dd, income: 0, expense: 0 });
        setData(final);
      } catch (err) {
        console.error(err);
        setData([]);
      } finally {
        setLoading(false);
      }
    };
    run();
  }, [userId, monthStart, monthEnd, start, end]);

  if (loading) return <div className="text-sm text-gray-500">Loading…</div>;
  if (!data.length) return <div className="text-sm text-gray-500">No data for this month.</div>;

  return (
    <div className="h-64">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="day" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="income" />
          <Line type="monotone" dataKey="expense" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

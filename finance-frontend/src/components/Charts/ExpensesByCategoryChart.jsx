import React, { useEffect, useState } from "react";
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";
import { getExpenseTotalsByCategory } from "../../services/api";

const COLORS = ["#8884d8", "#82ca9d", "#ffc658", "#ff7f50", "#00c49f", "#a78bfa", "#f59e0b"]; // consistent palette

export default function ExpensesByCategoryChart({ userId, start, end }) {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!userId || !start || !end) return;
    const run = async () => {
      setLoading(true);
      try {
        const res = await getExpenseTotalsByCategory(userId, start.toISOString(), end.toISOString());
        // res is a map { categoryName: amount }
        const arr = Object.entries(res || {}).map(([name, value]) => ({ name, value: Number(value || 0) }));
        setData(arr);
      } catch (err) {
        console.error(err);
        setData([]);
      } finally {
        setLoading(false);
      }
    };
    run();
  }, [userId, start, end]);

  if (loading) return <div className="text-sm text-gray-500">Loading…</div>;
  if (!data.length) return <div className="text-sm text-gray-500">No expense data in this range.</div>;

  return (
    <div className="h-64">
      <ResponsiveContainer width="100%" height="100%">
        <PieChart>
          <Pie data={data} dataKey="value" nameKey="name" outerRadius={90}>
            {data.map((_, i) => (
              <Cell key={i} fill={COLORS[i % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip />
          <Legend />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}

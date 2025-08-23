import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
} from "recharts";
import { getExpenseTotalsByCategory } from "../../services/api";

export default function MonthlyChart({ userId, refreshFlag, dateRange }) {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchMonthlyData = async () => {
      if (!dateRange?.start || !dateRange?.end) return;

      const startDate = new Date(dateRange.start);
      const endDate = new Date(dateRange.end);
      if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) return;

      // Set precise start/end timestamps
      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(23, 59, 59, 999);

      const totals = await getExpenseTotalsByCategory(
        userId,
        startDate.toISOString(),
        endDate.toISOString()
      );

      // Format data for chart
      const chartData = Object.entries(totals || {}).map(([category, amount]) => ({
        category,
        amount: parseFloat(amount.toFixed(2)),
      }));

      setData(chartData);
    };

    fetchMonthlyData();
  }, [userId, refreshFlag, dateRange.start, dateRange.end]);

  const tooltipFormatter = (value) => `₹ ${value.toLocaleString()}`;

  return (
    <ResponsiveContainer width="100%" height={350}>
      <BarChart
        data={data}
        margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid strokeDasharray="3 3" stroke="#ccc" />
        <XAxis dataKey="category" tick={{ fontSize: 12 }} />
        <YAxis tick={{ fontSize: 12 }} />
        <Tooltip formatter={tooltipFormatter} />
        <Bar dataKey="amount" fill="#4CAF50" barSize={40} radius={[5, 5, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  );
}

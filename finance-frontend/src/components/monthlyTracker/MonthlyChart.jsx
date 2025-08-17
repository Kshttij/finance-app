import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
  Cell,
} from "recharts";
import { getExpenseTotalsByCategory } from "../../services/api";

export default function MonthlyChart({ userId, refreshFlag, dateRange }) {
  const [data, setData] = useState([]);

  // Predefined color palette for categories
  const colors = [
    "#4CAF50", "#FF5722", "#2196F3", "#FFC107", "#9C27B0",
    "#00BCD4", "#E91E63", "#795548", "#FF9800", "#3F51B5"
  ];

  useEffect(() => {
    const fetchMonthlyData = async () => {
      if (!dateRange?.start || !dateRange?.end) return;

      const startDate = new Date(dateRange.start);
      const endDate = new Date(dateRange.end);
      if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) return;

      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(23, 59, 59, 999);

      const totals = await getExpenseTotalsByCategory(
        userId,
        startDate.toISOString(),
        endDate.toISOString()
      );

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
        <Bar dataKey="amount" barSize={40} radius={[5, 5, 0, 0]}>
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
}

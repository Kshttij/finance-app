import { useState, useEffect } from "react";
import TransactionForm from "./TransactionForm";
import TransactionList from "./TransactionList";
import MonthlyChart from "../tools/MonthlyChart";
import SummaryCard from "../tools/SummaryCard";
import FilterControls from "../tools/FilterControls";
import { getTotalByType } from "../../services/api";

export default function Dashboard({ userId }) {
  const [refreshFlag, setRefreshFlag] = useState(false);
  const [totalIncome, setTotalIncome] = useState(0);
  const [totalExpense, setTotalExpense] = useState(0);

  // Keep dateRange as Date objects
  const [dateRange, setDateRange] = useState(() => {
    const start = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
    start.setHours(0, 0, 0, 0);
    const end = new Date();
    end.setHours(23, 59, 59, 999);
    return { start, end };
  });

  const triggerRefresh = () => setRefreshFlag((prev) => !prev);

  useEffect(() => {
    const fetchTotals = async () => {
      const income = await getTotalByType(
        userId,
        "INCOME",
        dateRange.start.toISOString(),
        dateRange.end.toISOString()
      );
      const expense = await getTotalByType(
        userId,
        "EXPENSE",
        dateRange.start.toISOString(),
        dateRange.end.toISOString()
      );
      setTotalIncome(income);
      setTotalExpense(expense);
    };
    fetchTotals();
  }, [userId, refreshFlag, dateRange]);

  return (
    <div className="max-w-5xl mx-auto p-6">
      <h2 className="text-2xl font-bold text-center mb-2">Welcome to your Dashboard</h2>
      <p className="text-center text-gray-600 mb-6">User ID: {userId}</p>

      {/* Date filter */}
      <div className="mb-6 flex justify-center">
        <FilterControls dateRange={dateRange} setDateRange={setDateRange} />
      </div>

      {/* Summary cards */}
      <div className="flex flex-col sm:flex-row gap-4 justify-center mb-6">
        <SummaryCard title="Total Income" amount={totalIncome} color="bg-green-200" />
        <SummaryCard title="Total Expense" amount={totalExpense} color="bg-red-200" />
      </div>

      {/* Transaction Form */}
      <div className="mb-6 bg-white shadow rounded p-4">
        <TransactionForm userId={userId} onTransactionCreated={triggerRefresh} />
      </div>

      <hr className="my-6" />

      {/* Transaction List */}
      <div className="mb-6">
        <TransactionList
          userId={userId}
          refreshFlag={refreshFlag}
          onTransactionDeleted={triggerRefresh}
          dateRange={dateRange}
        />
      </div>

      {/* Chart */}
      <div className="mt-8 bg-white shadow rounded p-4">
        <h3 className="text-xl font-semibold mb-4 text-center">Monthly Expenses</h3>
        <MonthlyChart userId={userId} refreshFlag={refreshFlag} dateRange={dateRange} />
      </div>
    </div>
  );
}

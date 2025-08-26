// src/pages/Dashboard.jsx
import React, { useEffect, useMemo, useState } from "react";
import Navbar from "../components/Navbar";
import TransactionForm from "../components/TransactionForm";
import TransactionList from "../components/TransactionList";
import ExpensesByCategoryChart from "../components/Charts/ExpensesByCategoryChart";
import MonthlyTrendChart from "../components/Charts/MonthlyTrendChart";
import BudgetTracker from "../components/BudgetTracker";
import SavingsGoal from "../components/SavingsGoal";
import FilterControls from "../components/FilterControls";
import SummaryCard from "../components/SummaryCard";
import { getTransactionsInRange } from "../services/api";

function startOfCurrentMonth() {
  const d = new Date();
  return new Date(d.getFullYear(), d.getMonth(), 1, 0, 0, 0, 0);
}
function endOfCurrentMonth() {
  const d = new Date();
  return new Date(d.getFullYear(), d.getMonth() + 1, 0, 23, 59, 59, 999);
}

/**
 * Dashboard accepts either:
 * - a full user object: <Dashboard user={user} />
 * - or a raw userId prop: <Dashboard userId={123} />
 *
 * It will also fallback to localStorage keys "userId" or "auth" if needed.
 */
export default function Dashboard({ user, userId: userIdProp }) {
  // Resolve userId from user object, userIdProp, or localStorage
  const userId = useMemo(() => {
    // 1) prefer user object
    if (user && (user.id || user?.userId)) {
      return Number(user.id ?? user.userId);
    }
    // 2) prefer explicit prop
    if (userIdProp) return Number(userIdProp);

    // 3) fallback: localStorage.userId or localStorage.auth
    try {
      const storedId = localStorage.getItem("userId");
      if (storedId) return Number(storedId);

      const auth = JSON.parse(localStorage.getItem("auth"));
      if (auth) {
        if (auth.id) return Number(auth.id);
        if (auth.user && auth.user.id) return Number(auth.user.id);
        if (auth.userId) return Number(auth.userId);
      }
    } catch (e) {
      // ignore parse errors
    }
    return null;
  }, [user, userIdProp]);

  // Date range
  const [dateRange, setDateRange] = useState(() => ({
    start: startOfCurrentMonth(),
    end: endOfCurrentMonth(),
  }));

  const [refreshKey, setRefreshKey] = useState(0);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const triggerRefresh = () => setRefreshKey((k) => k + 1);

  // Fetch transactions for the selected range
  useEffect(() => {
    if (!userId) {
      setTransactions([]);
      return;
    }

    const fetchData = async () => {
      setLoading(true);
      setError("");
      try {
        const tx = await getTransactionsInRange(
          userId,
          dateRange.start.toISOString(),
          dateRange.end.toISOString()
        );
        setTransactions(Array.isArray(tx) ? tx : []);
      } catch (err) {
        console.error("Failed to load transactions:", err);
        setError("Failed to load transactions.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [userId, dateRange, refreshKey]);

  // Totals (range-based)
  const totals = useMemo(() => {
    let income = 0,
      expense = 0;
    for (const t of transactions) {
      if ((t?.type || "").toUpperCase() === "INCOME") income += Number(t.amount || 0);
      else if ((t?.type || "").toUpperCase() === "EXPENSE") expense += Number(t.amount || 0);
    }
    return { income, expense, net: income - expense };
  }, [transactions]);

  // Small debug line you can remove later:
  // console.log("Resolved userId for Dashboard:", userId, "user prop:", user);

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />

      <div className="max-w-7xl mx-auto p-4 sm:p-6">
        <div className="flex items-center justify-between gap-4 flex-wrap mb-4">
          <h1 className="text-2xl sm:text-3xl font-bold">Dashboard</h1>
          <FilterControls dateRange={dateRange} setDateRange={setDateRange} />
        </div>

        {/* Summary cards */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
          <SummaryCard title="Total Income" amount={totals.income} tone="positive" />
          <SummaryCard title="Total Expense" amount={totals.expense} tone="negative" />
          <SummaryCard title="Net" amount={totals.net} tone={totals.net >= 0 ? "positive" : "negative"} />
        </div>

        {/* Main content grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left column: Form + List */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white shadow rounded-2xl p-4">
              <h2 className="text-lg font-semibold mb-3">Add Transaction</h2>
              {/* pass numeric userId (or null) */}
              <TransactionForm userId={userId} onCreated={triggerRefresh} />
              {!userId && (
                <div className="mt-2 text-sm text-yellow-700 bg-yellow-50 p-2 rounded">
                  Could not resolve user id — please login again.
                </div>
              )}
            </div>

            <div className="bg-white shadow rounded-2xl p-4">
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-lg font-semibold">Transactions</h2>
                {loading && <span className="text-sm text-gray-500">Loading…</span>}
              </div>
              {error && (
                <div className="text-sm text-red-600 bg-red-50 border border-red-200 p-2 rounded mb-3">{error}</div>
              )}
              <TransactionList userId={userId} transactions={transactions} onChanged={triggerRefresh} />
            </div>
          </div>

          {/* Right column: Charts */}
          <div className="space-y-6">
            <div className="bg-white shadow rounded-2xl p-4">
              <h2 className="text-lg font-semibold mb-3">Expenses by Category</h2>
              <ExpensesByCategoryChart userId={userId} start={dateRange.start} end={dateRange.end} />
            </div>

            <div className="bg-white shadow rounded-2xl p-4">
              <h2 className="text-lg font-semibold mb-3">Daily Trend (This Range)</h2>
              <MonthlyTrendChart userId={userId} start={dateRange.start} end={dateRange.end} />
            </div>
          </div>
        </div>

        {/* Budgets & Savings */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
          <div className="bg-white shadow rounded-2xl p-4">
            <h2 className="text-lg font-semibold mb-3">Budget Tracker</h2>
            <BudgetTracker transactions={transactions} />
          </div>

          <div className="bg-white shadow rounded-2xl p-4">
            <h2 className="text-lg font-semibold mb-3">Savings Goals</h2>
            <SavingsGoal totals={totals} />
          </div>
        </div>
      </div>
    </div>
  );
}

import React, { useEffect, useMemo, useState } from "react";

// Local storage key
const KEY = "budget.tracker.v1";

export default function BudgetTracker({ transactions }) {
  const [budgets, setBudgets] = useState({}); // { [categoryName]: number }

  useEffect(() => {
    try {
      const saved = JSON.parse(localStorage.getItem(KEY));
      if (saved && typeof saved === "object") setBudgets(saved);
    } catch (_) {}
  }, []);

  useEffect(() => {
    try {
      localStorage.setItem(KEY, JSON.stringify(budgets));
    } catch (_) {}
  }, [budgets]);

  // Aggregate expenses by category (for current range)
  const expenseByCat = useMemo(() => {
    const map = {};
    for (const t of transactions || []) {
      if ((t?.type || "").toUpperCase() !== "EXPENSE") continue;
      const name = t?.category?.name || "(Uncategorized)";
      map[name] = (map[name] || 0) + Number(t.amount || 0);
    }
    return map;
  }, [transactions]);

  const categories = useMemo(() => Object.keys(expenseByCat).sort(), [expenseByCat]);

  const setBudget = (name, value) => {
    setBudgets((b) => ({ ...b, [name]: value ? Number(value) : 0 }));
  };

  return (
    <div className="space-y-3">
      {categories.length === 0 && (
        <div className="text-sm text-gray-500">No expense categories in this range.</div>
      )}

      {categories.map((name) => {
        const spent = expenseByCat[name] || 0;
        const limit = budgets[name] || 0;
        const pct = limit > 0 ? Math.min((spent / limit) * 100, 100) : 0;
        const over = limit > 0 && spent > limit;
        return (
          <div key={name} className="border rounded-xl p-3">
            <div className="flex items-center justify-between gap-3 mb-2">
              <div>
                <div className="font-medium">{name}</div>
                <div className="text-xs text-gray-500">Spent: ₹{spent.toLocaleString()} {limit > 0 && ` / Budget: ₹${limit.toLocaleString()}`}</div>
              </div>
              <input
                type="number"
                min={0}
                placeholder="Set budget"
                value={limit || ""}
                onChange={(e) => setBudget(name, e.target.value)}
                className="w-32 border rounded-lg px-2 py-1 text-sm"
              />
            </div>

            {limit > 0 ? (
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className={`h-2 rounded-full ${over ? "bg-rose-500" : "bg-emerald-500"}`}
                  style={{ width: `${pct}%` }}
                />
              </div>
            ) : (
              <div className="text-xs text-gray-500">Set a budget to track progress.</div>
            )}

            {over && (
              <div className="text-xs text-rose-600 mt-1">You exceeded the budget for {name}.</div>
            )}
          </div>
        );
      })}
    </div>
  );
}

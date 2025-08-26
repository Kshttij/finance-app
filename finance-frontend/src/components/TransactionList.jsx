import React, { useMemo, useState } from "react";
import { deleteTransaction } from "../services/api";

export default function TransactionList({ userId, transactions, onChanged }) {
  const [query, setQuery] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return [...(transactions || [])]
      .filter((t) => {
        if (typeFilter !== "ALL" && (t?.type || "") !== typeFilter) return false;
        if (!q) return true;
        const cat = t?.category?.name || "";
        const desc = t?.description || "";
        return cat.toLowerCase().includes(q) || desc.toLowerCase().includes(q);
      })
      .sort((a, b) => new Date(b.date) - new Date(a.date));
  }, [transactions, query, typeFilter]);

  const handleDelete = async (id) => {
    if (!id) return;
    if (!confirm("Delete this transaction?")) return;
    const ok = await deleteTransaction(userId, id);
    if (ok && onChanged) onChanged();
    if (!ok) alert("Failed to delete");
  };

  return (
    <div>
      <div className="flex items-center gap-2 mb-3 flex-wrap">
        <input
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search description/category"
          className="border rounded-lg px-3 py-1.5"
        />
        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
          className="border rounded-lg px-3 py-1.5"
        >
          <option value="ALL">All</option>
          <option value="INCOME">Income</option>
          <option value="EXPENSE">Expense</option>
        </select>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full text-sm">
          <thead>
            <tr className="text-left text-gray-500 border-b">
              <th className="py-2 pr-3">Date</th>
              <th className="py-2 pr-3">Category</th>
              <th className="py-2 pr-3">Description</th>
              <th className="py-2 pr-3">Type</th>
              <th className="py-2 pr-3 text-right">Amount</th>
              <th className="py-2 pr-3 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((t) => (
              <tr key={t.id} className="border-b">
                <td className="py-2 pr-3">{new Date(t.date).toLocaleString()}</td>
                <td className="py-2 pr-3">{t?.category?.name || "—"}</td>
                <td className="py-2 pr-3">{t?.description || "—"}</td>
                <td className="py-2 pr-3">
                  <span className={`px-2 py-0.5 rounded text-xs font-medium ${
                    (t?.type || "").toUpperCase() === "INCOME"
                      ? "bg-emerald-100 text-emerald-700"
                      : "bg-rose-100 text-rose-700"
                  }`}>
                    {t?.type}
                  </span>
                </td>
                <td className="py-2 pr-3 text-right font-semibold">₹{Number(t.amount || 0).toLocaleString()}</td>
                <td className="py-2 pr-3 text-right">
                  <button
                    onClick={() => handleDelete(t.id)}
                    className="text-rose-600 hover:underline"
                  >Delete</button>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td className="py-6 text-center text-gray-500" colSpan={6}>No transactions</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

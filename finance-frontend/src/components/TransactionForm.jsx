import React, { useEffect, useMemo, useState } from "react";
import { getCategoriesByType, createTransaction } from "../services/api";

export default function TransactionForm({ userId, onCreated }) {
  const [type, setType] = useState("EXPENSE"); // must be INCOME/EXPENSE for backend
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [date, setDate] = useState(() => {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    return now.toISOString().slice(0, 16); // yyyy-mm-ddThh:mm
  });
  const [saving, setSaving] = useState(false);

  // Load categories for selected type
  useEffect(() => {
    const run = async () => {
      const data = await getCategoriesByType(type);
      setCategories(Array.isArray(data) ? data : []);
      setCategoryId(data?.[0]?.id ?? "");
    };
    run();
  }, [type]);

  const canSubmit = useMemo(() => {
    return userId && amount && categoryId && type && date;
  }, [userId, amount, categoryId, type, date]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!canSubmit) return;
    setSaving(true);
    try {
      const payload = {
        type, // INCOME/EXPENSE
        categoryId: Number(categoryId),
        amount: parseFloat(amount),
        description,
        date: new Date(date).toISOString(),
      };
      const created = await createTransaction(userId, payload);
      if (created) {
        setAmount("");
        setDescription("");
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        setDate(now.toISOString().slice(0, 16));
        if (onCreated) onCreated(created);
      } else {
        alert("Failed to create transaction");
      }
    } catch (err) {
      console.error(err);
      alert("Something went wrong while saving");
    } finally {
      setSaving(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-6 gap-3">
      <select
        value={type}
        onChange={(e) => setType(e.target.value)}
        className="border rounded-lg px-3 py-2 md:col-span-1"
      >
        <option value="INCOME">Income</option>
        <option value="EXPENSE">Expense</option>
      </select>

      <select
        value={categoryId}
        onChange={(e) => setCategoryId(e.target.value)}
        className="border rounded-lg px-3 py-2 md:col-span-1"
      >
        {categories.length === 0 ? (
          <option value="">No categories</option>
        ) : (
          categories.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))
        )}
      </select>

      <input
        type="number"
        step="0.01"
        placeholder="Amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        className="border rounded-lg px-3 py-2 md:col-span-1"
        required
      />

      <input
        type="text"
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className="border rounded-lg px-3 py-2 md:col-span-1"
      />

      <input
        type="datetime-local"
        value={date}
        onChange={(e) => setDate(e.target.value)}
        className="border rounded-lg px-3 py-2 md:col-span-2"
      />

      <button
        type="submit"
        disabled={!canSubmit || saving}
        className="md:col-span-6 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded-lg shadow disabled:opacity-60"
      >
        {saving ? "Saving…" : "Add Transaction"}
      </button>
    </form>
  );
}

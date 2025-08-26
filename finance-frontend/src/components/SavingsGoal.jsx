import React, { useEffect, useState } from "react";

const KEY = "savings.goals.v1";

export default function SavingsGoal({ totals }) {
  const [goals, setGoals] = useState([]); // [{ id, name, target, saved }]
  const [form, setForm] = useState({ name: "", target: "", saved: "" });

  useEffect(() => {
    try {
      const saved = JSON.parse(localStorage.getItem(KEY));
      if (Array.isArray(saved)) setGoals(saved);
    } catch (_) {}
  }, []);

  useEffect(() => {
    try {
      localStorage.setItem(KEY, JSON.stringify(goals));
    } catch (_) {}
  }, [goals]);

  const addGoal = (e) => {
    e.preventDefault();
    if (!form.name || !form.target) return;
    setGoals((g) => [
      ...g,
      { id: crypto.randomUUID(), name: form.name, target: Number(form.target), saved: Number(form.saved || 0) }
    ]);
    setForm({ name: "", target: "", saved: "" });
  };

  const updateSaved = (id, saved) => {
    setGoals((g) => g.map((x) => (x.id === id ? { ...x, saved: Number(saved || 0) } : x)));
  };

  const removeGoal = (id) => setGoals((g) => g.filter((x) => x.id !== id));

  const disposable = Math.max(totals.net || 0, 0);

  return (
    <div className="space-y-4">
      <form onSubmit={addGoal} className="grid grid-cols-1 md:grid-cols-4 gap-3">
        <input
          placeholder="Goal name (e.g., Laptop)"
          value={form.name}
          onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
          className="border rounded-lg px-3 py-2"
        />
        <input
          type="number"
          placeholder="Target amount"
          value={form.target}
          onChange={(e) => setForm((f) => ({ ...f, target: e.target.value }))}
          className="border rounded-lg px-3 py-2"
        />
        <input
          type="number"
          placeholder="Already saved (optional)"
          value={form.saved}
          onChange={(e) => setForm((f) => ({ ...f, saved: e.target.value }))}
          className="border rounded-lg px-3 py-2"
        />
        <button className="bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg px-3 py-2 font-medium">Add Goal</button>
      </form>

      {goals.length === 0 && (
        <div className="text-sm text-gray-500">No goals yet. Add your first goal above.</div>
      )}

      <div className="space-y-3">
        {goals.map((g) => {
          const pct = Math.min(((g.saved || 0) / (g.target || 1)) * 100, 100);
          return (
            <div key={g.id} className="border rounded-xl p-3">
              <div className="flex items-center justify-between gap-3">
                <div>
                  <div className="font-medium">{g.name}</div>
                  <div className="text-xs text-gray-500">₹{(g.saved || 0).toLocaleString()} / ₹{(g.target || 0).toLocaleString()} ({pct.toFixed(1)}%)</div>
                </div>
                <button onClick={() => removeGoal(g.id)} className="text-rose-600 hover:underline text-sm">Remove</button>
              </div>

              <div className="w-full bg-gray-200 rounded-full h-2 mt-2">
                <div className="h-2 rounded-full bg-indigo-600" style={{ width: `${pct}%` }} />
              </div>

              <div className="mt-3 flex items-center gap-2">
                <span className="text-xs text-gray-500">Update saved:</span>
                <input
                  type="number"
                  value={g.saved}
                  onChange={(e) => updateSaved(g.id, e.target.value)}
                  className="border rounded-lg px-2 py-1 text-sm w-32"
                />
                {disposable > 0 && (
                  <span className="text-xs text-gray-500">You can allocate up to ₹{Math.floor(disposable).toLocaleString()} from current net.</span>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}

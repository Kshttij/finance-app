import React from "react";

export default function FilterControls({ dateRange, setDateRange }) {
  const onChange = (key) => (e) => {
    const value = e.target.value;
    const parts = value.split("-");
    // If datetime-local is used, adapt here. Currently using date only for simplicity.
    const dt = new Date(parts[0], parts[1] - 1, parts[2]);
    setDateRange((r) => ({ ...r, [key]: dt }));
  };

  const fmtDateInput = (d) => {
    // yyyy-mm-dd for <input type="date">
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${y}-${m}-${day}`;
  };

  const setQuickRange = (name) => {
    const now = new Date();
    if (name === "thisMonth") {
      const start = new Date(now.getFullYear(), now.getMonth(), 1);
      const end = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59, 999);
      setDateRange({ start, end });
    } else if (name === "last30") {
      const end = new Date();
      const start = new Date();
      start.setDate(end.getDate() - 29);
      start.setHours(0, 0, 0, 0);
      end.setHours(23, 59, 59, 999);
      setDateRange({ start, end });
    }
  };

  return (
    <div className="flex items-center gap-2 flex-wrap">
      <input
        type="date"
        value={fmtDateInput(dateRange.start)}
        onChange={onChange("start")}
        className="border rounded-lg px-3 py-1.5"
      />
      <span className="text-gray-500">to</span>
      <input
        type="date"
        value={fmtDateInput(dateRange.end)}
        onChange={onChange("end")}
        className="border rounded-lg px-3 py-1.5"
      />
      <button
        className="px-3 py-1.5 rounded-lg bg-gray-200 hover:bg-gray-300 text-sm"
        onClick={() => setQuickRange("thisMonth")}
      >This month</button>
      <button
        className="px-3 py-1.5 rounded-lg bg-gray-200 hover:bg-gray-300 text-sm"
        onClick={() => setQuickRange("last30")}
      >Last 30 days</button>
    </div>
  );
}

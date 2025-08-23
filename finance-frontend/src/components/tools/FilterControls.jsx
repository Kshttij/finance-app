import React from "react";

export default function FilterControls({ dateRange, setDateRange }) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (!value) return;
    const newDate = new Date(value);
    if (isNaN(newDate.getTime())) return;
    if (name === "start") newDate.setHours(0, 0, 0, 0);
    if (name === "end") newDate.setHours(23, 59, 59, 999);
    setDateRange({ ...dateRange, [name]: newDate });
  };

  return (
    <div className="flex flex-col sm:flex-row gap-4 items-center justify-center">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Start Date</label>
        <input
          type="date"
          name="start"
          value={dateRange.start.toISOString().slice(0, 10)}
          onChange={handleChange}
          className="border rounded px-3 py-1 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">End Date</label>
        <input
          type="date"
          name="end"
          value={dateRange.end.toISOString().slice(0, 10)}
          onChange={handleChange}
          className="border rounded px-3 py-1 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
      </div>
    </div>
  );
}

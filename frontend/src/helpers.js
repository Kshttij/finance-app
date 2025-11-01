// src/helpers.js

// ✅ We keep 'waait' for any demo/testing
export const waait = () => new Promise((res) => setTimeout(res, Math.random() * 800));

// ✅ We keep the color generator
export const generateRandomColor = () => {
  // This is now just for visual flair, not tied to data length
  const hue = Math.floor(Math.random() * 360);
  return `${hue} 65% 50%`;
};

// ❌ DELETED: fetchData, getAllMatchingItems, deleteItem, createBudget, createExpense
// (All this logic is now in our loaders, actions, and api.js)

// ✅ MODIFIED: This function now requires the expenses array to be passed in.
// This makes it a "pure" function and much easier to test and read.
export const calculateSpentByBudget = (budgetId, expenses) => {
  if (!expenses || !budgetId) return 0;

  const budgetSpent = expenses.reduce((acc, expense) => {
    // Check if the expense belongs to the budget
    if (expense.budgetId !== budgetId) return acc;
    // Add the expense amount to the accumulator
    return acc + (Number(expense.amount) || 0);
  }, 0);
  
  return budgetSpent;
};


// --- Formatting Helpers (Unchanged) ---

export const formatDateToLocaleString = (epoch) =>
  new Date(epoch).toLocaleDateString();

export const formatPercentage = (amt) => {
  let value = Number(amt);
  return value.toLocaleString(undefined, {
    style: "percent",
    minimumFractionDigits: 0,
  });
};

export const formatCurrency = (amt) => {
  return Number(amt).toLocaleString(undefined, {
    style: "currency",
    currency: "USD",
  });
};
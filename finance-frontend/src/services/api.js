
const API_URL = "https://finance-app-1-nqo6.onrender.com"; 

// -------------------- Auth APIs --------------------
export async function signup(userData) {
  const response = await fetch(`${API_URL}/signup`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(userData),
  });
  return response.json();
}

export async function login(userData) {
  const response = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(userData),
  });
  return response.json();
}

// -------------------- Categories --------------------
export async function getCategoriesByType(type) {
  const res = await fetch(`${API_URL}/api/categories/by-type?type=${type}`);
  return res.ok ? res.json() : [];
}

// -------------------- Transactions --------------------
export async function createTransaction(userId, transactionData) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(transactionData),
  });
  return res.ok;
}

export async function getTransactions(userId) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions`);
  return res.json();
}

export async function updateTransaction(userId, transactionId, transactionData) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions/${transactionId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(transactionData),
  });
  return res.ok;
}

export async function deleteTransaction(userId, transactionId) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions/${transactionId}`, {
    method: "DELETE",
  });
  return res.ok;
}

export async function getTransactionsByType(userId, type) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions/type/${type}`);
  return res.json();
}

export async function getTransactionsByCategory(userId, categoryId) {
  const res = await fetch(`${API_URL}/users/${userId}/transactions/category/${categoryId}`);
  return res.json();
}

export async function getMonthlyTransactions(userId, startOfMonth, endOfMonth) {
  const res = await fetch(
    `${API_URL}/users/${userId}/transactions/monthly?startOfMonth=${startOfMonth}&endOfMonth=${endOfMonth}`
  );
  return res.json();
}

export async function getTransactionsInRange(userId, start, end) {
  const res = await fetch(
    `${API_URL}/users/${userId}/transactions/range?start=${start}&end=${end}`
  );
  return res.ok ? res.json() : [];
}

// -------------------- Analytics --------------------
export async function getExpenseTotalsByCategory(userId, start, end) {
  const res = await fetch(
    `${API_URL}/users/${userId}/analytics/expenses-by-category?start=${start}&end=${end}`
  );
  return res.ok ? res.json() : {};
}

export async function getTotalByType(userId, type) {
  const res = await fetch(`${API_URL}/users/${userId}/analytics/total/${type}`);
  return res.ok ? res.json() : 0;
}

